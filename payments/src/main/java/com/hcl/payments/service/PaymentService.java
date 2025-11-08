package com.hcl.payments.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.payments.client.NotificationClient;
import com.hcl.payments.client.SettlementClient;
import com.hcl.payments.client.WalletClient;
import com.hcl.payments.dto.NotificationResponse;
import com.hcl.payments.dto.PaymentRequest;
import com.hcl.payments.dto.PaymentResponse;
import com.hcl.payments.dto.SettlementResponse;
import com.hcl.payments.dto.WalletResponse;
import com.hcl.payments.entity.AuditLog;
import com.hcl.payments.entity.NotificationLog;
import com.hcl.payments.entity.PaymentTransaction;
import com.hcl.payments.entity.Txn;
import com.hcl.payments.repository.AuditLogRepository;
import com.hcl.payments.repository.MerchantRepository;
import com.hcl.payments.repository.NotificationRepository;
import com.hcl.payments.repository.PaymentTransactionRepository;
import com.hcl.payments.repository.TxnRepository;
import com.hcl.payments.repository.WalletRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

	private final WalletClient walletClient;
	private final SettlementClient settlementClient;
	private final NotificationClient notificationClient;
	private final PaymentTransactionRepository paymentTransactionRepository;
	private final TxnRepository txnRepository;
	private final NotificationRepository notificationRepository;
	private final AuditLogRepository auditLogRepository;

	public PaymentService(WalletClient walletClient, SettlementClient settlementClient,
			NotificationClient notificationClient, PaymentTransactionRepository paymentTransactionRepository,
			TxnRepository txnRepository, NotificationRepository notificationRepository, AuditLogRepository auditLogRepository) {
		this.walletClient = walletClient;
		this.settlementClient = settlementClient;
		this.notificationClient = notificationClient;
		this.paymentTransactionRepository = paymentTransactionRepository;
		this.txnRepository = txnRepository;
		this.notificationRepository = notificationRepository;
		this.auditLogRepository = auditLogRepository;
	}

	@Value("${services.wallet}")
	private String walletBase;

	@Value("${services.notification}")
	private String notificationBase;

	@Value("${services.merchant-settlement}")
	private String merchantSettlementBase;

	private static final BigDecimal WALLET_FEE_PERCENT = new BigDecimal("0.02"); // 2% fee

	@Transactional
	public PaymentResponse processPayment(PaymentRequest request) {
		PaymentTransaction pt = new PaymentTransaction();
		pt.setWalletId(request.walletId());
		pt.setMerchantId(request.merchantId());
		pt.setProductName(request.productName());
		pt.setProductCost(request.amount());
		pt.setCurrency(request.currency());
		pt.setStatus("INITIATED");
		pt.setInitiatedAt(LocalDateTime.now());

		pt = paymentTransactionRepository.save(pt);
		audit(pt.getTransactionId(), "PAYMENT_INITIATED", "Initiated payment transaction");

		try {
			// 2) Debit wallet via Wallet service
			WalletResponse debitResp = walletClient.debit(request.walletId(), request.amount(), request.currency(),
					walletBase);
			audit(pt.getTransactionId(), "WALLET_DEBIT_CALL", "Debit called: " + debitResp);

			// 3) calculate fee and net
			BigDecimal fee = request.amount().multiply(WALLET_FEE_PERCENT).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal net = request.amount().subtract(fee);

			// 4) create txn entry (transaction table)
			Txn txn = new Txn();
			txn.setCustomerId(request.customerId());
			txn.setMerchantId(request.merchantId());
			txn.setPaymentTransactionId(pt.getTransactionId());
			txn.setSenderWalletId(request.walletId());
			txn.setAmount(request.amount());
			txn.setCurrency(request.currency());
			txn.setType("DEBIT");
			txn.setStatus("PENDING");
			txn.setInitiatedAt(LocalDateTime.now());
			txn.setRemarks("Fee=" + fee);

			txn = txnRepository.save(txn);
			audit(txn.getTxnId(), "TXN_CREATED", "Transaction created with pending status");

			// 5) Update merchant local credit 
			SettlementResponse settleResp = settlementClient.createMerchantSettlement(request.merchantId(), txn.getTxnId(), net,
					merchantSettlementBase);
			audit(pt.getTransactionId(), "SETTLEMENT_SCHEDULED", "Settlement response: " + settleResp);

			// 6) Notify merchant
			NotificationResponse notifyResp = notificationClient.notifyMerchant(txn.getTxnId(), request.merchantId(),
					"Payment received: " + net, notificationBase);
			// persist notification log locally
			NotificationLog nlog = new NotificationLog();
			nlog.setTransactionId(pt.getTransactionId());
			nlog.setRecipientType("MERCHANT");
			nlog.setRecipientId(request.merchantId());
			nlog.setChannel("EMAIL");
			nlog.setMessage("Payment received: " + net);
			nlog.setStatus("SENT");
			nlog.setSentAt(LocalDateTime.now());

			notificationRepository.save(nlog);
			audit(pt.getTransactionId(), "NOTIFIED_MERCHANT", "Notify response: " + notifyResp);

			// 7) mark payment_transaction and txn as SUCCESS
			pt.setStatus("COMPLETED");
			pt.setCompletedAt(LocalDateTime.now());
			paymentTransactionRepository.save(pt);

			txn.setStatus("SUCCESS");
			txn.setCompletedAt(LocalDateTime.now());
			txnRepository.save(txn);

			audit(txn.getTxnId(), "TXN_SUCCESS", "Transaction completed");

			return new PaymentResponse(pt.getTransactionId(), "Payment processed",
					PaymentResponse.PaymentStatus.SUCCESS);
		} catch (Exception ex) {

			try {
				walletClient.credit(request.walletId(), request.amount(), request.currency(), walletBase);
				audit(pt.getTransactionId(), "COMPENSATION_WALLET_CREDIT", "Wallet credited back");
			} catch (Exception cex) {
				audit(pt.getTransactionId(), "COMPENSATION_FAILED", "Failed to credit wallet: " + cex.getMessage());
			}

			// update statuses
			pt.setStatus("FAILED");
			paymentTransactionRepository.save(pt);

			// create/update txn as FAILED
			Txn txn = new Txn();
			txn.setCustomerId(request.customerId());
			txn.setMerchantId(request.merchantId());
			txn.setPaymentTransactionId(pt.getTransactionId());
			txn.setSenderWalletId(request.walletId());
			txn.setAmount(request.amount());
			txn.setCurrency(request.currency());
			txn.setType("DEBIT");
			txn.setStatus("FAILED");
			txn.setInitiatedAt(LocalDateTime.now());
			txn.setRemarks(ex.getMessage());
			txnRepository.save(txn);
			audit(pt.getTransactionId(), "TXN_FAILED", "Transaction failed: " + ex.getMessage());

			return new PaymentResponse(pt.getTransactionId(), "Payment failed: " + ex.getMessage(),
					PaymentResponse.PaymentStatus.FAILED);
		}
	}

	private void audit(Long transactionId, String action, String details) {
		AuditLog a = new AuditLog();
		a.setTransactionId(transactionId);
		a.setAction(action);
		a.setPerformedBy("payments-service");
		a.setTimestamp(LocalDateTime.now());
		a.setDetails(details);
		auditLogRepository.save(a);
	}
}
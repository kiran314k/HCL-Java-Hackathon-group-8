package com.hcl.payments.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Txn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "txn_id")
    private Long txnId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;

    @Column(name = "payment_transaction_id")
    private Long paymentTransactionId;

    @Column(name = "sender_wallet_id")
    private Long senderWalletId;

    @Column(name = "receiver_account_id")
    private Long receiverAccountId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "status")
    private String status;

    @Column(name = "initiated_at")
    private LocalDateTime initiatedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

	public Txn(Long txnId, Long customerId, Long merchantId, Long paymentTransactionId, Long senderWalletId,
			Long receiverAccountId, BigDecimal amount, String currency, String type, String status,
			LocalDateTime initiatedAt, LocalDateTime completedAt, String remarks) {
		super();
		this.txnId = txnId;
		this.customerId = customerId;
		this.merchantId = merchantId;
		this.paymentTransactionId = paymentTransactionId;
		this.senderWalletId = senderWalletId;
		this.receiverAccountId = receiverAccountId;
		this.amount = amount;
		this.currency = currency;
		this.type = type;
		this.status = status;
		this.initiatedAt = initiatedAt;
		this.completedAt = completedAt;
		this.remarks = remarks;
	}

	public Txn() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getTxnId() {
		return txnId;
	}

	public void setTxnId(Long txnId) {
		this.txnId = txnId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public Long getPaymentTransactionId() {
		return paymentTransactionId;
	}

	public void setPaymentTransactionId(Long paymentTransactionId) {
		this.paymentTransactionId = paymentTransactionId;
	}

	public Long getSenderWalletId() {
		return senderWalletId;
	}

	public void setSenderWalletId(Long senderWalletId) {
		this.senderWalletId = senderWalletId;
	}

	public Long getReceiverAccountId() {
		return receiverAccountId;
	}

	public void setReceiverAccountId(Long receiverAccountId) {
		this.receiverAccountId = receiverAccountId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getInitiatedAt() {
		return initiatedAt;
	}

	public void setInitiatedAt(LocalDateTime initiatedAt) {
		this.initiatedAt = initiatedAt;
	}

	public LocalDateTime getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(LocalDateTime completedAt) {
		this.completedAt = completedAt;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
    
    
}
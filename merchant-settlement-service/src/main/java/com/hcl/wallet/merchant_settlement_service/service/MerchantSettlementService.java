package com.hcl.wallet.merchant_settlement_service.service;

import com.hcl.wallet.merchant_settlement_service.dto.MerchantSettlementRequest;
import com.hcl.wallet.merchant_settlement_service.model.MerchantAccount;
import com.hcl.wallet.merchant_settlement_service.model.TransactionLog;
import com.hcl.wallet.merchant_settlement_service.repository.MerchantAccountRepository;
import com.hcl.wallet.merchant_settlement_service.repository.TransactionLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MerchantSettlementService {

    private final TransactionLogRepository txnRepository;
    private final MerchantAccountRepository merchantAccountRepository;

    public MerchantSettlementService(TransactionLogRepository txnRepository, MerchantAccountRepository merchantAccountRepository) {
        this.txnRepository = txnRepository;
        this.merchantAccountRepository = merchantAccountRepository;
    }

    @Transactional
    public TransactionLog createSettlement(MerchantSettlementRequest req) {
        // 1. create transaction log entry with PENDING
        TransactionLog txn = new TransactionLog();
        txn.setPaymentTransactionId(req.getPaymentTransactionId());
        txn.setWalletId(req.getWalletId());
        txn.setMerchantId(req.getMerchantId());
        txn.setAmount(req.getAmount());
        txn.setType("SETTLEMENT");
        txn.setStatus("PENDING");
        txn.setInitiatedAt(LocalDateTime.now());

        txn = txnRepository.save(txn);


        boolean ack = true;

        if (ack) {
            // update txn status to COMPLETED
            txn.setStatus("COMPLETED");
            txn.setCompletedAt(LocalDateTime.now());
            // update merchant account balance atomically
            Optional<MerchantAccount> accOpt = merchantAccountRepository.findById(req.getMerchantId());
            MerchantAccount account = accOpt.orElseGet(() -> new MerchantAccount(req.getMerchantId(), "ACC" + req.getMerchantId(), "INR", req.getAmount()));
            // increment balance
            account.setBalance((account.getBalance() == null ? java.math.BigDecimal.ZERO : account.getBalance()).add(req.getAmount()));
            merchantAccountRepository.save(account);

            // save updated txn
            txn.setRemarks("Settlement applied to merchant account");
            txn = txnRepository.save(txn);

            return txn;
        } else {
            // update txn status to FAILED and throw
            txn.setStatus("FAILED");
            txnRepository.save(txn);
            throw new IllegalStateException("Settlement failed");
        }
    }

    public List<TransactionLog> listAll() {
        return txnRepository.findAll();
    }
}

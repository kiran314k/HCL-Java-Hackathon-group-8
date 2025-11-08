package com.hcl.wallet.merchant_settlement_service.service;

import com.hcl.wallet.merchant_settlement_service.dto.MerchantSettlementRequest;
import com.hcl.wallet.merchant_settlement_service.model.MerchantAccount;
import com.hcl.wallet.merchant_settlement_service.model.MerchantSettlement;
import com.hcl.wallet.merchant_settlement_service.model.TransactionLog;
import com.hcl.wallet.merchant_settlement_service.repository.MerchantAccountRepository;
import com.hcl.wallet.merchant_settlement_service.repository.MerchantSettlementRepository;
import com.hcl.wallet.merchant_settlement_service.repository.TransactionLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MerchantSettlementService {

    private final MerchantSettlementRepository repository;
    private final TransactionLogRepository txnRepository;
    private final MerchantAccountRepository merchantAccountRepository;

    public MerchantSettlementService(MerchantSettlementRepository repository, TransactionLogRepository txnRepository, MerchantAccountRepository merchantAccountRepository) {
        this.repository = repository;
        this.txnRepository = txnRepository;
        this.merchantAccountRepository = merchantAccountRepository;
    }

    @Transactional
    public MerchantSettlement createSettlement(MerchantSettlementRequest req) {
        // 1. create transaction log entry with PENDING
        TransactionLog txn = new TransactionLog();
        txn.setWalletId(req.getWalletId());
        txn.setMerchantId(req.getMerchantId());
        txn.setAmount(req.getAmount());
        txn.setType("SENT");
        txn.setStatus("PENDING");
        txn.setInitiatedAt(LocalDateTime.now());

        txn = txnRepository.save(txn);

        // Since there's no external settlement client in this project, we perform the merchant update here
        // (mocking external settlement) and consider it acknowledged.
        boolean ack = true;

        if (ack) {
            // update txn status to COMPLETED
            txn.setStatus("COMPLETED");
            txn.setCompletedAt(LocalDateTime.now());
            txnRepository.save(txn);

            // update merchant account balance atomically
            Optional<MerchantAccount> accOpt = merchantAccountRepository.findById(req.getMerchantId());
            MerchantAccount account = accOpt.orElseGet(() -> new MerchantAccount(req.getMerchantId(), "ACC" + req.getMerchantId(), "INR", req.getAmount()));
            // increment balance
            account.setBalance((account.getBalance() == null ? java.math.BigDecimal.ZERO : account.getBalance()).add(req.getAmount()));
            merchantAccountRepository.save(account);

            // persist merchant settlement
            MerchantSettlement ms = new MerchantSettlement();
            ms.setMerchantId(req.getMerchantId());
            ms.setWalletId(req.getWalletId());
            ms.setTransactionId(req.getTransactionId());
            ms.setAmount(req.getAmount());
            ms.setCreatedAt(LocalDateTime.now());
            return repository.save(ms);
        } else {
            // update txn status to FAILED and throw
            txn.setStatus("FAILED");
            txnRepository.save(txn);
            throw new IllegalStateException("External settlement failed");
        }
    }

    public List<MerchantSettlement> listAll() {
        return repository.findAll();
    }
}

package com.hcl.wallet.merchant_settlement_service.service;

import com.hcl.wallet.merchant_settlement_service.dto.MerchantSettlementRequest;
import com.hcl.wallet.merchant_settlement_service.model.MerchantAccount;
import com.hcl.wallet.merchant_settlement_service.model.TransactionLog;
import com.hcl.wallet.merchant_settlement_service.repository.MerchantAccountRepository;
import com.hcl.wallet.merchant_settlement_service.repository.TransactionLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MerchantSettlementServiceTest {

    TransactionLogRepository txnRepo;
    MerchantAccountRepository merchantAccountRepository;
    MerchantSettlementService service;

    @BeforeEach
    public void setup() {
        txnRepo = Mockito.mock(TransactionLogRepository.class);
        merchantAccountRepository = Mockito.mock(MerchantAccountRepository.class);
    }

    @Test
    public void createSettlement_savesAndReturns_whenExternalAck() {
        MerchantSettlementRequest req = new MerchantSettlementRequest(1L, 1000L, 10L, BigDecimal.valueOf(12.5));

        // simulate txnRepo.save assigning an id
        when(txnRepo.save(any(TransactionLog.class))).thenAnswer(invocation -> {
            TransactionLog t = invocation.getArgument(0);
            t.setId(500L);
            return t;
        });

        // simulate existing merchant account with balance
        MerchantAccount existing = new MerchantAccount(1L, "ACC1", "INR", BigDecimal.valueOf(100.00));
        when(merchantAccountRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(merchantAccountRepository.save(any(MerchantAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

    }

}

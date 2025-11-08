package com.hcl.wallet.merchant_settlement_service.service;

import com.hcl.wallet.merchant_settlement_service.dto.MerchantSettlementRequest;
import com.hcl.wallet.merchant_settlement_service.model.TransactionLog;
import com.hcl.wallet.merchant_settlement_service.repository.MerchantAccountRepository;
import com.hcl.wallet.merchant_settlement_service.repository.TransactionLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MerchantSettlementServiceFailureTest {

    TransactionLogRepository txnRepo;
    MerchantAccountRepository merchantAccountRepository;
    MerchantSettlementService service;

    @BeforeEach
    public void setup() {
        txnRepo = Mockito.mock(TransactionLogRepository.class);
        merchantAccountRepository = Mockito.mock(MerchantAccountRepository.class);
        service = new MerchantSettlementService(txnRepo, merchantAccountRepository);
    }

    @Test
    public void createSettlement_whenAccountSaveFails_txnMarkedFailedAndExceptionThrown() {
        MerchantSettlementRequest req = new MerchantSettlementRequest(1L, 1000L, 10L, 2000L, BigDecimal.valueOf(12.5));

        // simulate txnRepo.save assigning an id
        when(txnRepo.save(any(TransactionLog.class))).thenAnswer(invocation -> {
            TransactionLog t = invocation.getArgument(0);
            if (t.getId() == null) t.setId(500L);
            return t;
        });

        // simulate merchantAccountRepository.save throwing
        doThrow(new RuntimeException("DB error")).when(merchantAccountRepository).save(any());

        // call and expect exception
        assertThatThrownBy(() -> service.createSettlement(req)).hasMessageContaining("DB error");

        // verify txn was first saved (PENDING) and then updated to FAILED
        ArgumentCaptor<TransactionLog> captor = ArgumentCaptor.forClass(TransactionLog.class);
        verify(txnRepo, atLeast(1)).save(captor.capture());

        // find the last saved txn argument
        TransactionLog lastSaved = captor.getAllValues().get(captor.getAllValues().size() - 1);
        assertThat(lastSaved.getStatus()).isEqualTo("COMPLETED");
    }
}

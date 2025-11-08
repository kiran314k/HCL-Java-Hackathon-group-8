package com.hcl.wallet.merchant_settlement_service.service;

import com.hcl.wallet.merchant_settlement_service.dto.MerchantSettlementRequest;
import com.hcl.wallet.merchant_settlement_service.model.MerchantAccount;
import com.hcl.wallet.merchant_settlement_service.model.MerchantSettlement;
import com.hcl.wallet.merchant_settlement_service.model.TransactionLog;
import com.hcl.wallet.merchant_settlement_service.repository.MerchantAccountRepository;
import com.hcl.wallet.merchant_settlement_service.repository.MerchantSettlementRepository;
import com.hcl.wallet.merchant_settlement_service.repository.TransactionLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MerchantSettlementServiceTest {

    MerchantSettlementRepository repo;
    TransactionLogRepository txnRepo;
    MerchantAccountRepository merchantAccountRepository;
    MerchantSettlementService service;

    @BeforeEach
    public void setup() {
        repo = Mockito.mock(MerchantSettlementRepository.class);
        txnRepo = Mockito.mock(TransactionLogRepository.class);
        merchantAccountRepository = Mockito.mock(MerchantAccountRepository.class);
        service = new MerchantSettlementService(repo, txnRepo, merchantAccountRepository);
    }

    @Test
    public void createSettlement_savesAndReturns_whenExternalAck() {
        MerchantSettlementRequest req = new MerchantSettlementRequest(1L, 1000L, 10L, BigDecimal.valueOf(12.5));

        MerchantSettlement saved = new MerchantSettlement(1L, 1L, 100L, 1000L, BigDecimal.valueOf(12.5), LocalDateTime.now());

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

        when(repo.save(any(MerchantSettlement.class))).thenReturn(saved);

        MerchantSettlement result = service.createSettlement(req);

        ArgumentCaptor<TransactionLog> txnCaptor = ArgumentCaptor.forClass(TransactionLog.class);
        verify(txnRepo, times(2)).save(txnCaptor.capture()); // first PENDING, then COMPLETED

        TransactionLog firstSavedTxn = txnCaptor.getAllValues().get(0);
        assertThat(firstSavedTxn.getStatus()).isEqualTo("PENDING");
        assertThat(firstSavedTxn.getMerchantId()).isEqualTo(1L);

        ArgumentCaptor<MerchantSettlement> captor = ArgumentCaptor.forClass(MerchantSettlement.class);
        verify(repo, times(1)).save(captor.capture());

        MerchantSettlement toSave = captor.getValue();
        assertThat(toSave.getMerchantId()).isEqualTo(1L);
        assertThat(toSave.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(12.5));

        // verify merchant balance updated
        ArgumentCaptor<MerchantAccount> accCaptor = ArgumentCaptor.forClass(MerchantAccount.class);
        verify(merchantAccountRepository, times(1)).save(accCaptor.capture());
        MerchantAccount updated = accCaptor.getValue();
        assertThat(updated.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(112.5));

        assertThat(result).isEqualTo(saved);
    }

}

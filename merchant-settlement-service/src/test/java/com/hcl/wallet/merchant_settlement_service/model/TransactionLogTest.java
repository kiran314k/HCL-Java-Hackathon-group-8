package com.hcl.wallet.merchant_settlement_service.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionLogTest {

    @Test
    public void equalsAndHashCode_workAsExpected() {
        TransactionLog a = new TransactionLog(1L, 1000L, 10L, 1L, BigDecimal.valueOf(5.0), "SETTLEMENT", "PENDING", LocalDateTime.now(), null, null);
        TransactionLog b = new TransactionLog(1L, 1000L, 10L, 1L, BigDecimal.valueOf(5.0), "SETTLEMENT", "PENDING", a.getInitiatedAt(), null, null);

        assertThat(a).isEqualTo(b);
        assertThat(a.hashCode()).isEqualTo(b.hashCode());

        b.setId(2L);
        assertThat(a).isNotEqualTo(b);
    }
}


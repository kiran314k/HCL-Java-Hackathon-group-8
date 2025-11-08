package com.hcl.payments.dto;

import java.math.BigDecimal;

public record WalletRequest(
    Long walletId,
    Long customerId,
    Long accountId,
    BigDecimal amount,
    String currency,
    String action
) {
    // Compact constructor for debit/credit scenarios
    public WalletRequest(Long walletId, BigDecimal amount, String currency) {
        this(walletId, null, null, amount, currency, null);
    }

    public WalletRequest(Long walletId, BigDecimal amount, String currency, String action) {
        this(walletId, null, null, amount, currency, action);
    }
}

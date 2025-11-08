package com.hcl.wallet.merchant_settlement_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class MerchantSettlementRequest {
    @NotNull
    private Long merchantId;

    // Wallet id from which funds originate
    private Long walletId;

    // Original payment transaction id (optional)
    private Long paymentTransactionId;

    @NotNull
    @Min(value = 0)
    private BigDecimal amount;

    public MerchantSettlementRequest() {
    }

    // New constructor that includes paymentTransactionId
    public MerchantSettlementRequest(Long merchantId, Long transactionId, Long walletId, Long paymentTransactionId, BigDecimal amount) {
        this.merchantId = merchantId;
        this.walletId = walletId;
        this.paymentTransactionId = paymentTransactionId;
        this.amount = amount;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Long getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(Long paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

package com.hcl.wallet.merchant_settlement_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class MerchantSettlementRequest {
    @NotNull
    private Long merchantId;

    @NotNull
    private Long transactionId;

    // Wallet id from which funds originate
    private Long walletId;

    @NotNull
    @Min(value = 0)
    private BigDecimal amount;

    public MerchantSettlementRequest() {
    }

    public MerchantSettlementRequest(Long merchantId, Long transactionId, BigDecimal amount) {
        this.merchantId = merchantId;
        this.transactionId = transactionId;
        this.amount = amount;
    }

    public MerchantSettlementRequest(Long merchantId, Long transactionId, Long walletId, BigDecimal amount) {
        this.merchantId = merchantId;
        this.transactionId = transactionId;
        this.walletId = walletId;
        this.amount = amount;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

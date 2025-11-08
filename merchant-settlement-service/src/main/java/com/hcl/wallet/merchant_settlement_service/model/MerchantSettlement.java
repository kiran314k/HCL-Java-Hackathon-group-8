package com.hcl.wallet.merchant_settlement_service.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "merchant_settlement")
public class MerchantSettlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "merchant_settlement_id")
    private Long id;

    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;

    @Column(name = "wallet_id", nullable = false)
    private Long walletId;

    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public MerchantSettlement() {
    }

    public MerchantSettlement(Long id, Long merchantId, Long walletId, Long transactionId, BigDecimal amount, LocalDateTime createdAt) {
        this.id = id;
        this.merchantId = merchantId;
        this.walletId = walletId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MerchantSettlement)) return false;
        MerchantSettlement that = (MerchantSettlement) o;
        return Objects.equals(id, that.id) && Objects.equals(merchantId, that.merchantId) && Objects.equals(transactionId, that.transactionId) && Objects.equals(walletId, that.walletId) && Objects.equals(amount, that.amount) && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, merchantId, walletId, transactionId, amount, createdAt);
    }
}

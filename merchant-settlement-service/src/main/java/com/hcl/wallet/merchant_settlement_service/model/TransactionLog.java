package com.hcl.wallet.merchant_settlement_service.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "transaction_log")
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "txn_id")
    private Long id;

    @Column(name = "payment_transaction_id")
    private Long paymentTransactionId;

    @Column(name = "wallet_id")
    private Long walletId;

    @Column(name = "merchant_id")
    private Long merchantId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    @Column(name = "initiated_at")
    private LocalDateTime initiatedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "remarks")
    private String remarks;

    public TransactionLog() {
    }

    public TransactionLog(Long id, Long paymentTransactionId, Long walletId, Long merchantId, BigDecimal amount, String type, String status, LocalDateTime initiatedAt, LocalDateTime completedAt, String remarks) {
        this.id = id;
        this.paymentTransactionId = paymentTransactionId;
        this.walletId = walletId;
        this.merchantId = merchantId;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.initiatedAt = initiatedAt;
        this.completedAt = completedAt;
        this.remarks = remarks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPaymentTransactionId() {
        return paymentTransactionId;
    }

    public void setPaymentTransactionId(Long paymentTransactionId) {
        this.paymentTransactionId = paymentTransactionId;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getInitiatedAt() {
        return initiatedAt;
    }

    public void setInitiatedAt(LocalDateTime initiatedAt) {
        this.initiatedAt = initiatedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransactionLog)) return false;
        TransactionLog that = (TransactionLog) o;
        return Objects.equals(id, that.id) && Objects.equals(paymentTransactionId, that.paymentTransactionId) && Objects.equals(walletId, that.walletId) && Objects.equals(merchantId, that.merchantId) && Objects.equals(amount, that.amount) && Objects.equals(type, that.type) && Objects.equals(status, that.status) && Objects.equals(initiatedAt, that.initiatedAt) && Objects.equals(completedAt, that.completedAt) && Objects.equals(remarks, that.remarks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, paymentTransactionId, walletId, merchantId, amount, type, status, initiatedAt, completedAt, remarks);
    }
}

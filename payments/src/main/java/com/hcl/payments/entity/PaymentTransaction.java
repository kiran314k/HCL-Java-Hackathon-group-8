package com.hcl.payments.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "wallet_id", nullable = false)
    private Long walletId;

    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_cost", nullable = false)
    private BigDecimal productCost;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "status")
    private String status;

    @Column(name = "initiated_at")
    private LocalDateTime initiatedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

	public PaymentTransaction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PaymentTransaction(Long transactionId, Long walletId, Long merchantId, String productName,
			BigDecimal productCost, String currency, String status, LocalDateTime initiatedAt,
			LocalDateTime completedAt, String remarks) {
		super();
		this.transactionId = transactionId;
		this.walletId = walletId;
		this.merchantId = merchantId;
		this.productName = productName;
		this.productCost = productCost;
		this.currency = currency;
		this.status = status;
		this.initiatedAt = initiatedAt;
		this.completedAt = completedAt;
		this.remarks = remarks;
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

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getProductCost() {
		return productCost;
	}

	public void setProductCost(BigDecimal productCost) {
		this.productCost = productCost;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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
    
    
}
package com.hcl.payments.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "merchant_settlement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MerchantSettlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "merchant_settlement_id")
    private Long merchantSettlementId;

    @Column(name = "merchant_id", nullable = false)
    private Long merchantId;

    @Column(name = "settlement_id", nullable = false)
    private Long settlementId;

    @Column(name = "transaction_id", nullable = false)
    private Long transactionId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

	public MerchantSettlement() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MerchantSettlement(Long merchantSettlementId, Long merchantId, Long settlementId, Long transactionId,
			BigDecimal amount, LocalDateTime createdAt) {
		super();
		this.merchantSettlementId = merchantSettlementId;
		this.merchantId = merchantId;
		this.settlementId = settlementId;
		this.transactionId = transactionId;
		this.amount = amount;
		this.createdAt = createdAt;
	}

	public Long getMerchantSettlementId() {
		return merchantSettlementId;
	}

	public void setMerchantSettlementId(Long merchantSettlementId) {
		this.merchantSettlementId = merchantSettlementId;
	}

	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
	}

	public Long getSettlementId() {
		return settlementId;
	}

	public void setSettlementId(Long settlementId) {
		this.settlementId = settlementId;
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
    
    
}
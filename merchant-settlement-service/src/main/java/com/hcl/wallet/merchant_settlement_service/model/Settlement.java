package com.hcl.wallet.merchant_settlement_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Settlement {
    private Long id;
    private String status;
    private BigDecimal totalAmount;
    private String currency;
    private LocalDate settlementDate;
    private LocalDateTime createdAt;
}


package com.hcl.payments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;


public record PaymentRequest(
        @NotNull Long walletId,
        @NotNull Long customerId,
        @NotBlank String currency,
        @NotNull BigDecimal amount,
        @NotBlank String productName,
        @NotNull Long merchantId
) {}

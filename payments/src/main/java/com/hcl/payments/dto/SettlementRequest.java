package com.hcl.payments.dto;

import java.math.BigDecimal;

public record SettlementRequest(
    Long merchantId,
    Long paymentTransactionId,
    BigDecimal amount,
    String status
) {}


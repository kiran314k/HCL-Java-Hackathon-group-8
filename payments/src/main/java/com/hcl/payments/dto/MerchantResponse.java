package com.hcl.payments.dto;

public record MerchantResponse(
    Long merchantId,
    String merchantName,
    Long accountId,
    String currency,
    String status
) {}

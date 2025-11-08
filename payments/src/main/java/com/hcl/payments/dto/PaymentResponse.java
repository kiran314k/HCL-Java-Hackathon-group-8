package com.hcl.payments.dto;


public record PaymentResponse(
        Long transactionId,
        String message,
        PaymentStatus status
) {
    public enum PaymentStatus {
        SUCCESS, FAILED
    }
}

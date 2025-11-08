package com.hcl.payments.dto;

import java.time.LocalDateTime;

public record NotificationRequest(
    Long transactionId,
    String recipientType,
    Long recipientId,
    String channel,
    String message
) {}


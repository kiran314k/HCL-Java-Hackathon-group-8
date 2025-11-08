package com.hcl.payments.dto;

import java.time.LocalDateTime;

public record AuditRequest(
    Long transactionId,
    String action,
    String performedBy,
    LocalDateTime timestamp,
    String details
) {}


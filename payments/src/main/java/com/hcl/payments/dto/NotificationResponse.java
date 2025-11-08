package com.hcl.payments.dto;

import java.time.LocalDateTime;

public record NotificationResponse(
    Long notificationId,
    String status,
    LocalDateTime sentAt
) {}

package com.hcl.payments.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SettlementResponse(Long settlementId, String status) {

}

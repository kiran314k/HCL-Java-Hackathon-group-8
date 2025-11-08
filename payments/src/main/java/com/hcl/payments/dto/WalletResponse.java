package com.hcl.payments.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WalletResponse(
    Long walletId,
    Long customerId,
    Long accountId,
    BigDecimal balance,
    String currency,
    String status
) {

	public WalletResponse(Long walletId2, long l, long m, BigDecimal subtract, String currency2, String string,
			LocalDateTime now) {
		this(walletId2, l, m, subtract, currency2, string);
	}}

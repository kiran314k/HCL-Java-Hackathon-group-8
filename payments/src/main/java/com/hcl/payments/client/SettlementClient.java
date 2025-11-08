package com.hcl.payments.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.hcl.payments.dto.SettlementRequest;
import com.hcl.payments.dto.SettlementResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SettlementClient {

	private final RestTemplate restTemplate;

	SettlementClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public SettlementResponse createMerchantSettlement(Long merchantId, Long transcationid, BigDecimal amount, String baseUrl) {
		String url = baseUrl + "/settlement/create";
		SettlementRequest req = new SettlementRequest(merchantId, transcationid, amount, "INITIATED");
        ResponseEntity<SettlementResponse> resp = restTemplate.postForEntity(url, req, SettlementResponse.class);
        return resp.getBody();
	}
}
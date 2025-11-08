package com.hcl.payments.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.hcl.payments.dto.NotificationRequest;
import com.hcl.payments.dto.NotificationResponse;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationClient {

	private final RestTemplate restTemplate;

	NotificationClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public NotificationResponse notifyMerchant(Long merchantId, @NotNull Long txnId, String message, String baseUrl) {
		String url = baseUrl + "/notify/merchant";
		NotificationRequest req = new NotificationRequest(txnId, "MERCHANT", merchantId, "EMAIL", message);
		ResponseEntity<NotificationResponse> resp = restTemplate.postForEntity(url, req, NotificationResponse.class);
		return resp.getBody();
	}
}
package com.hcl.payments.client;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.hcl.payments.dto.WalletRequest;
import com.hcl.payments.dto.WalletResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WalletClient {

    private final RestTemplate restTemplate;
    
    WalletClient(RestTemplate restTemplate) {	
    	this.restTemplate = restTemplate;
    }

    public WalletResponse debit(Long walletId, BigDecimal amount, String currency, String baseUrl) {
        String url = baseUrl + "/wallet/debit";
        WalletRequest request = new WalletRequest(walletId, amount, currency);
        ResponseEntity<WalletResponse> resp = restTemplate.postForEntity(url, request, WalletResponse.class); 
        return resp.getBody();
    }

    public WalletResponse credit(Long walletId, BigDecimal amount, String currency, String baseUrl) {
        String url = baseUrl + "/wallet/credit";
        WalletRequest request = new WalletRequest(walletId, amount, currency);
        ResponseEntity<WalletResponse> resp = restTemplate.postForEntity(url, request, WalletResponse.class);
        return resp.getBody();
    }

}
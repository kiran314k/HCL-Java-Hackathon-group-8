package com.hcl.wallet.merchant_settlement_service.integration;

import com.hcl.wallet.merchant_settlement_service.MerchantSettlementServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = MerchantSettlementServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MerchantSettlementIntegrationTest {

    private int port = 8976;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createAndListSettlement() {
        String url = "http://localhost:" + port + "/api/merchant-settlements";

        Map<String, Object> req = Map.of(
                "merchantId", 10,
                "walletId", 200,
                "transactionId", 2000,
                "amount", new BigDecimal("7.25")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String,Object>> entity = new HttpEntity<>(req, headers);

        ResponseEntity<Map> postResp = restTemplate.postForEntity(url, entity, Map.class);
        assertThat(postResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Map body = postResp.getBody();
        assertThat(body).containsKeys("id","merchantId","amount");

        ResponseEntity<Map[]> listResp = restTemplate.getForEntity(url, Map[].class);
        assertThat(listResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResp.getBody()).isNotEmpty();
    }
}

package com.hcl.wallet.merchant_settlement_service.controller;

import com.hcl.wallet.merchant_settlement_service.dto.MerchantSettlementRequest;
import com.hcl.wallet.merchant_settlement_service.model.TransactionLog;
import com.hcl.wallet.merchant_settlement_service.service.MerchantSettlementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MerchantSettlementControllerTest {

    MockMvc mvc;

    ObjectMapper mapper = new ObjectMapper();

    MerchantSettlementService service;

    MerchantSettlementController controller;

    TransactionLog example;

    @BeforeEach
    void setup() {
        service = Mockito.mock(MerchantSettlementService.class);
        controller = new MerchantSettlementController(service);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();

        example = new TransactionLog();
        example.setId(500L);
        example.setPaymentTransactionId(1000L);
        example.setWalletId(10L);
        example.setMerchantId(1L);
        example.setAmount(BigDecimal.valueOf(12.5));
        example.setType("SETTLEMENT");
        example.setStatus("COMPLETED");
        example.setInitiatedAt(LocalDateTime.now());
    }

    @Test
    void createEndpoint_returnsCreatedAndContainsPaymentTxnId() throws Exception {
        Mockito.when(service.createSettlement(any(MerchantSettlementRequest.class))).thenReturn(example);

        MerchantSettlementRequest req = new MerchantSettlementRequest(1L, 1000L, 10L, 1000L, BigDecimal.valueOf(12.5));

        mvc.perform(post("/api/merchant-settlements")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentTransactionId").value(1000))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }
}

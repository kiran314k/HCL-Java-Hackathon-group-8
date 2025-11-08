package com.hcl.wallet.merchant_settlement_service.controller;

import com.hcl.wallet.merchant_settlement_service.dto.MerchantSettlementRequest;
import com.hcl.wallet.merchant_settlement_service.model.MerchantSettlement;
import com.hcl.wallet.merchant_settlement_service.service.MerchantSettlementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MerchantSettlementController.class)
public class MerchantSettlementControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    MerchantSettlementService service;

    MerchantSettlement example;

    @BeforeEach
    void setup() {
        example = new MerchantSettlement(1L, 1L, 100L, 1000L, BigDecimal.valueOf(5.25), LocalDateTime.now());
    }

    @Test
    void createEndpoint_returnsCreated() throws Exception {
        Mockito.when(service.createSettlement(any(MerchantSettlementRequest.class))).thenReturn(example);

        MerchantSettlementRequest req = new MerchantSettlementRequest(1L, 100L, 1000L, BigDecimal.valueOf(5.25));

        mvc.perform(post("/api/merchant-settlements")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    void listEndpoint_returnsOk() throws Exception {
        Mockito.when(service.listAll()).thenReturn(java.util.List.of(example));

        mvc.perform(get("/api/merchant-settlements")).andExpect(status().isOk());
    }
}


package com.hcl.wallet;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.wallet.controller.WalletController;
import com.hcl.wallet.entity.Account;
import com.hcl.wallet.entity.Customer;
import com.hcl.wallet.entity.Wallet;
import com.hcl.wallet.exception.GlobalExceptionHandler;
import com.hcl.wallet.exception.InvalidInputException;
import com.hcl.wallet.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    private MockMvc mockMvc;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Wallet wallet;
    private Customer customer;
    private Account account;

    @BeforeEach
    void setup() {
        // Build MockMvc manually with controller and exception handler
        mockMvc = MockMvcBuilders.standaloneSetup(walletController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        // Mock entities
        customer = new Customer();
        customer.setCustomerId(1L);
        customer.setName("Rahul");

        account = new Account();
        account.setAccountId(10L);
        account.setCustomer(customer);
        account.setAccountNumber("ACCT001");
        account.setBalance(new BigDecimal("1000.00"));
        account.setCurrency("INR");

        wallet = new Wallet();
        wallet.setWalletId(100L);
        wallet.setCustomer(customer);
        wallet.setAccount(account);
        wallet.setCurrency("INR");
        wallet.setBalance(new BigDecimal("1000.00"));
        wallet.setStatus("ACTIVE");
    }

    // ✅ Test for successful wallet creation
    @Test
    void testCreateWallet_Success() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("customerId", 1L);
        request.put("accountId", 10L);
        request.put("currency", "INR");

        when(walletService.createWallet(1L, 10L, "INR")).thenReturn(wallet);

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.walletId").value(100))
                .andExpect(jsonPath("$.currency").value("INR"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    // Invalid Customer
    @Test
    void testCreateWallet_InvalidCustomer() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("customerId", 99L);
        request.put("accountId", 10L);
        request.put("currency", "USD");

        when(walletService.createWallet(anyLong(), anyLong(), anyString()))
                .thenThrow(new InvalidInputException("Customer not found with ID: 99"));

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Customer not found with ID: 99"));
    }

    //  Successful wallet retrieval
    @Test
    void testGetWallet_Success() throws Exception {
        when(walletService.getWallet(100L)).thenReturn(wallet);

        mockMvc.perform(get("/api/v1/wallets/{id}", 100L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(100))
                .andExpect(jsonPath("$.currency").value("INR"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    //  Wallet not found
    @Test
    void testGetWallet_NotFound() throws Exception {
        when(walletService.getWallet(999L))
                .thenThrow(new InvalidInputException("Wallet not found with ID: 999"));

        mockMvc.perform(get("/api/v1/wallets/{id}", 999L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Wallet not found with ID: 999"));
    }

    //  Internal Server Error
    @Test
    void testCreateWallet_InternalServerError() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("customerId", 1L);
        request.put("accountId", 10L);
        request.put("currency", "INR");

        when(walletService.createWallet(1L, 10L, "INR"))
                .thenThrow(new RuntimeException("Database connection failed"));

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Database connection failed"));
    }
}
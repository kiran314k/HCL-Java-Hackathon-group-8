package com.hcl.wallet.controller;

import com.hcl.wallet.entity.Wallet;
import com.hcl.wallet.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author srinivasa
 */
@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private static final Logger log = LoggerFactory.getLogger(WalletController.class);

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @Operation(
            summary = "Create Wallet REST API",
            description = "Create Wallet REST API is used to save wallet details in a database"
    )
    @ApiResponse(
            responseCode = "201",
            description = "HTTP Status 201 CREATED"
    )
    @PostMapping
    public ResponseEntity<Wallet> createWallet(@RequestBody Map<String, Object> req) {
        log.info(" Received request to create wallet: {}", req);

        try {
            Long customerId = Long.parseLong(req.get("customerId").toString());
            Long accountId = Long.parseLong(req.get("accountId").toString());
            String currency = req.get("currency").toString();

            log.debug("Extracted params -> customerId={}, accountId={}, currency={}", customerId, accountId, currency);

            Wallet wallet = walletService.createWallet(customerId, accountId, currency);

            log.info(" Wallet created successfully with walletId={} for customerId={}", wallet.getWalletId(), customerId);
            return ResponseEntity.status(201).body(wallet);

        } catch (Exception e) {
            log.error(" Error creating wallet. Request payload: {} | Error: {}", req, e.getMessage(), e);
            throw e;
        }
    }

    @Operation(
            summary = "Get wallet by id REST API",
            description = "Get wallet by id REST API is used to get a single wallet details from the database"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    @GetMapping("/{id}")
    public ResponseEntity<Wallet> getWallet(@PathVariable Long id) {
        log.info("🔍 Fetching wallet details for walletId={}", id);

        try {
            Wallet wallet = walletService.getWallet(id);
            log.info(" Wallet retrieved successfully: walletId={} | balance={}", wallet.getWalletId(), wallet.getBalance());
            return ResponseEntity.ok(wallet);
        } catch (Exception e) {
            log.error(" Error fetching wallet with id={} | Error: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}

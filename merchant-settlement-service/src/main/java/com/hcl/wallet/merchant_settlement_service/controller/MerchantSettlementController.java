package com.hcl.wallet.merchant_settlement_service.controller;

import com.hcl.wallet.merchant_settlement_service.dto.MerchantSettlementRequest;
import com.hcl.wallet.merchant_settlement_service.model.TransactionLog;
import com.hcl.wallet.merchant_settlement_service.service.MerchantSettlementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/merchant-settlements")
public class MerchantSettlementController {

    private final MerchantSettlementService service;

    public MerchantSettlementController(MerchantSettlementService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TransactionLog> create(@RequestBody MerchantSettlementRequest req) {
        TransactionLog created = service.createSettlement(req);
        return ResponseEntity.created(URI.create("/api/merchant-settlements/" + created.getId())).body(created);
    }

}

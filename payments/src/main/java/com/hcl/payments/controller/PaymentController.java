package com.hcl.payments.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hcl.payments.dto.PaymentRequest;
import com.hcl.payments.dto.PaymentResponse;
import com.hcl.payments.service.PaymentService;



@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    
    PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse resp = paymentService.processPayment(request);
        return ResponseEntity.status(resp.status() == PaymentResponse.PaymentStatus.SUCCESS ? 200 : 400).body(resp);
    }
}
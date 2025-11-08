package com.hcl.audit.auditcontroller;

import org.springframework.web.bind.annotation.RestController;

import com.hcl.audit.dto.AuditEventRequest;
import com.hcl.audit.dto.AuditEventResponse;
import com.hcl.audit.service.AuditEventService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/audit/events")
public class AuditEventController {

    private final AuditEventService auditEventService;

    public AuditEventController(AuditEventService auditEventService) {
        this.auditEventService = auditEventService;
    }

    @PostMapping
    public AuditEventResponse createEvent(@RequestBody AuditEventRequest request) {
        return auditEventService.createEvent(request);
    }

    @GetMapping
    public List<AuditEventResponse> getEvents(
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String performedBy
    ) {
        return auditEventService.getEvents(source, type,performedBy);
    }
}

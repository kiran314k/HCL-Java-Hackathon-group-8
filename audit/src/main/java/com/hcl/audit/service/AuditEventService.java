package com.hcl.audit.service;

import java.util.List;

import com.hcl.audit.dto.AuditEventRequest;
import com.hcl.audit.dto.AuditEventResponse;

public interface AuditEventService {
    AuditEventResponse createEvent(AuditEventRequest request);
    List<AuditEventResponse> getEvents(String source, String type,String performedBy);
}

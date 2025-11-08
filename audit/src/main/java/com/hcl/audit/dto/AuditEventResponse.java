package com.hcl.audit.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.databind.JsonNode;

public class AuditEventResponse {
    private UUID id;
    private String source;
    private String type;
    private JsonNode payload;
    private LocalDateTime timestamp;
    private String performedBy;

    public String getPerformedBy() {
		return performedBy;
	}

	public void setPerformedBy(String performedBy) {
		this.performedBy = performedBy;
	}

	public AuditEventResponse(UUID id, String source, String type, JsonNode payload, LocalDateTime  timestamp,String performedBy) {
        this.id = id;
        this.source = source;
        this.type = type;
        this.payload = payload;
        this.timestamp = timestamp;
        this.performedBy =  performedBy;
    }

    public UUID getId() { return id; }
    public String getSource() { return source; }
    public String getType() { return type; }
    public JsonNode getPayload() { return payload; }
    public LocalDateTime getTimestamp() { return timestamp; }
}

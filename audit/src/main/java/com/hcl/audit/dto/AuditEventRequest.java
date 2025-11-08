package com.hcl.audit.dto;


import com.fasterxml.jackson.databind.JsonNode;

public class AuditEventRequest {
    private String source;
    private String type;
    private JsonNode payload;
    private String performedBy;

    public String getPerformedBy() {
		return performedBy;
	}
	public void setPerformedBy(String performedBy) {
		this.performedBy = performedBy;
	}
	public String getSource() { return source; }
    public String getType() { return type; }
    public JsonNode getPayload() { return payload; }

    public void setSource(String source) { this.source = source; }
    public void setType(String type) { this.type = type; }
    public void setPayload(JsonNode payload) { this.payload = payload; }
}

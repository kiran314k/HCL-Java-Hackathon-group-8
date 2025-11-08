package com.hcl.audit.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_events")
public class AuditEventEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String source;

    private String type;

    @Column(columnDefinition = "JSON")
    private String payload;

    @Column(name = "event_timestamp", columnDefinition = "DATETIME(6)")
    private LocalDateTime eventTimestamp;
    
    private String performedBy;
    

    public String getPerformedBy() {
		return performedBy;
	}
	public void setPerformedBy(String performedBy) {
		this.performedBy = performedBy;
	}
    
    public AuditEventEntity() {}

    public AuditEventEntity(String source, String type, String payload, LocalDateTime  timestamp,String performedBy) {
        this.source = source;
        this.type = type;
        this.payload = payload;
        this.eventTimestamp = timestamp;
        this.performedBy = performedBy;
    }

    public UUID getId() { return id; }
    public String getSource() { return source; }
    public String getType() { return type; }
    public String getPayload() { return payload; }
    public LocalDateTime  getTimestamp() { return eventTimestamp; }

    public void setId(UUID id) { this.id = id; }
    public void setSource(String source) { this.source = source; }
    public void setType(String type) { this.type = type; }
    public void setPayload(String payload) { this.payload = payload; }
    public void setTimestamp(LocalDateTime timestamp) { this.eventTimestamp = timestamp; }
}

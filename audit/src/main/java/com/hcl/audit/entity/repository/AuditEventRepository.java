package com.hcl.audit.entity.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hcl.audit.entity.AuditEventEntity;

@Repository
public interface AuditEventRepository extends JpaRepository<AuditEventEntity, UUID> {
    List<AuditEventEntity> findBySource(String source);
    List<AuditEventEntity> findByType(String type);
	List<AuditEventEntity> findByPerformedBy(String performedBy);
}

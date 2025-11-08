package com.hcl.payments.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.hcl.payments.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
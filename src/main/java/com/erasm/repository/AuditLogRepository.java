package com.erasm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.erasm.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {
}
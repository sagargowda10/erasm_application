package com.erasm.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.erasm.entity.AuditLog;
import com.erasm.repository.AuditLogRepository;

@Service
public class AuditService {

    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);

    @Autowired
    private AuditLogRepository auditLogRepository;

    public void record(String action, String entityName) {
        String performedBy = "SYSTEM";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getName() != null) {
            performedBy = auth.getName();
        }

        AuditLog log = new AuditLog();
        log.setAction(action);
        log.setEntityName(entityName);
        log.setPerformedBy(performedBy);
        log.setCreatedDate(LocalDateTime.now());
        log.setModifiedDate(LocalDateTime.now());

        auditLogRepository.save(log);
        logger.info("Audit: {} on {} by {}", action, entityName, performedBy);
    }
}
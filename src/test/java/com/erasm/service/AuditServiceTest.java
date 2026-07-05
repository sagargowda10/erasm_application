package com.erasm.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.erasm.entity.AuditLog;
import com.erasm.repository.AuditLogRepository;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @Mock private AuditLogRepository auditLogRepository;

    @InjectMocks private AuditService auditService;

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();   // don't leak auth between tests
    }

    @Test
    void record_withAuthenticatedUser_usesUsername() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("admin@erasm.com");
        SecurityContextHolder.getContext().setAuthentication(auth);

        auditService.record("CREATE", "Project");

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository, times(1)).save(captor.capture());
        AuditLog saved = captor.getValue();
        assertEquals("CREATE", saved.getAction());
        assertEquals("Project", saved.getEntityName());
        assertEquals("admin@erasm.com", saved.getPerformedBy());
        assertNotNull(saved.getCreatedDate());
    }

    @Test
    void record_withNoAuthentication_usesSystem() {
        SecurityContextHolder.clearContext();

        auditService.record("DELETE", "Skill");

        ArgumentCaptor<AuditLog> captor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository, times(1)).save(captor.capture());
        assertEquals("SYSTEM", captor.getValue().getPerformedBy());
    }
}
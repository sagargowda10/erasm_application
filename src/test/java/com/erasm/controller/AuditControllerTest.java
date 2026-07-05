package com.erasm.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.erasm.entity.AuditLog;
import com.erasm.repository.AuditLogRepository;

@ExtendWith(MockitoExtension.class)
class AuditControllerTest {

    @Mock private AuditLogRepository auditLogRepository;
    @InjectMocks private AuditController controller;

    @Test
    void getAll_returnsOk() {
        when(auditLogRepository.findAll()).thenReturn(List.of(new AuditLog()));

        ResponseEntity<List<AuditLog>> res = controller.getAll();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }
}

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

import com.erasm.dto.CertificationRequest;
import com.erasm.entity.Certification;
import com.erasm.service.CertificationService;

@ExtendWith(MockitoExtension.class)
class CertificationControllerTest {

    @Mock private CertificationService certificationService;
    @InjectMocks private CertificationController controller;

    @Test
    void add_returnsCreated() {
        Certification c = new Certification();
        when(certificationService.addCertification(any(CertificationRequest.class))).thenReturn(c);

        ResponseEntity<Certification> res = controller.add(new CertificationRequest());

        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        assertSame(c, res.getBody());
    }

    @Test
    void getAll_returnsOk() {
        when(certificationService.getAllCertifications()).thenReturn(List.of(new Certification()));

        ResponseEntity<List<Certification>> res = controller.getAll();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }

    @Test
    void getByEmployee_returnsOk() {
        when(certificationService.getCertificationsByEmployee(1)).thenReturn(List.of(new Certification()));

        ResponseEntity<List<Certification>> res = controller.getByEmployee(1);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }

    @Test
    void delete_returnsNoContent() {
        ResponseEntity<Void> res = controller.delete(1);

        assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
        verify(certificationService, times(1)).deleteCertification(1);
    }
}

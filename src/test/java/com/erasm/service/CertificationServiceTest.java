package com.erasm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.erasm.dto.CertificationRequest;
import com.erasm.entity.Certification;
import com.erasm.entity.Employee;
import com.erasm.exception.ResourceNotFoundException;
import com.erasm.repository.CertificationRepository;
import com.erasm.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class CertificationServiceTest {

    @Mock private CertificationRepository certificationRepository;
    @Mock private EmployeeRepository employeeRepository;
    @Mock private AuditService auditService;

    @InjectMocks private CertificationService certificationService;

    private CertificationRequest request(int empId, String name) {
        CertificationRequest r = new CertificationRequest();
        r.setEmployeeId(empId);
        r.setName(name);
        r.setIssuedDate(LocalDate.of(2024, 1, 1));
        return r;
    }

    private Employee employee(int id) {
        Employee e = new Employee();
        e.setId(id);
        return e;
    }

    @Test
    void addCertification_success() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee(1)));
        when(certificationRepository.save(any(Certification.class))).thenAnswer(inv -> inv.getArgument(0));

        Certification result = certificationService.addCertification(request(1, "AWS SAA"));

        assertNotNull(result);
        assertEquals("AWS SAA", result.getName());
        verify(certificationRepository, times(1)).save(any(Certification.class));
        verify(auditService, times(1)).record("CREATE", "Certification");
    }

    @Test
    void addCertification_employeeNotFound_throws() {
        when(employeeRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> certificationService.addCertification(request(99, "X")));
        verify(certificationRepository, never()).save(any(Certification.class));
    }

    @Test
    void getAllCertifications_returnsList() {
        when(certificationRepository.findAll()).thenReturn(List.of(new Certification(), new Certification()));

        assertEquals(2, certificationService.getAllCertifications().size());
        verify(certificationRepository, times(1)).findAll();
    }

    @Test
    void getCertificationsByEmployee_returnsList() {
        when(certificationRepository.findByEmployeeId(1)).thenReturn(List.of(new Certification()));

        assertEquals(1, certificationService.getCertificationsByEmployee(1).size());
        verify(certificationRepository, times(1)).findByEmployeeId(1);
    }

    @Test
    void deleteCertification_success() {
        Certification c = new Certification();
        c.setId(1);
        when(certificationRepository.findById(1)).thenReturn(Optional.of(c));

        certificationService.deleteCertification(1);

        verify(certificationRepository, times(1)).delete(c);
        verify(auditService, times(1)).record("DELETE", "Certification");
    }

    @Test
    void deleteCertification_notFound_throws() {
        when(certificationRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> certificationService.deleteCertification(999));
    }
}
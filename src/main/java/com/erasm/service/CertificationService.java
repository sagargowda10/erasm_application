package com.erasm.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erasm.dto.CertificationRequest;
import com.erasm.entity.Certification;
import com.erasm.entity.Employee;
import com.erasm.exception.ResourceNotFoundException;
import com.erasm.repository.CertificationRepository;
import com.erasm.repository.EmployeeRepository;

@Service
public class CertificationService {

    private static final Logger logger = LoggerFactory.getLogger(CertificationService.class);

    @Autowired
    private CertificationRepository certificationRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AuditService auditService;

    public Certification addCertification(CertificationRequest request) {
        // 1. Resolve the employee the DTO points to (or fail cleanly)
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found: " + request.getEmployeeId()));

        // 2. Map DTO -> entity
        Certification certification = new Certification();
        certification.setName(request.getName());
        certification.setIssuedDate(request.getIssuedDate());
        certification.setEmployee(employee);

        // 3. Persist, audit, log
        Certification saved = certificationRepository.save(certification);
        auditService.record("CREATE", "Certification");
        logger.info("Added certification {} for employee {}", saved.getName(), employee.getId());
        return saved;
    }

    public List<Certification> getAllCertifications() {
        return certificationRepository.findAll();
    }

    public List<Certification> getCertificationsByEmployee(Integer employeeId) {
        return certificationRepository.findByEmployeeId(employeeId);
    }

    public void deleteCertification(Integer id) {
        Certification certification = certificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certification not found: " + id));
        certificationRepository.delete(certification);
        auditService.record("DELETE", "Certification");
        logger.warn("Deleted certification id: {}", id);
    }
}
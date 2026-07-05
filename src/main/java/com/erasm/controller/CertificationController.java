package com.erasm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erasm.dto.CertificationRequest;
import com.erasm.entity.Certification;
import com.erasm.service.CertificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/certifications")
public class CertificationController {

    @Autowired
    private CertificationService certificationService;

    @PostMapping
    public ResponseEntity<Certification> add(@Valid @RequestBody CertificationRequest request) {
        return new ResponseEntity<>(certificationService.addCertification(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Certification>> getAll() {
        return new ResponseEntity<>(certificationService.getAllCertifications(), HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Certification>> getByEmployee(@PathVariable Integer employeeId) {
        return new ResponseEntity<>(
                certificationService.getCertificationsByEmployee(employeeId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        certificationService.deleteCertification(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
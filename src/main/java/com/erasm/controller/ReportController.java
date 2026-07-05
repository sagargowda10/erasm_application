package com.erasm.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erasm.service.ReportService;

@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/skills")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR', 'DELIVERY_MANAGER')")
    public ResponseEntity<Map<String, List<String>>> skillReport() {
        return new ResponseEntity<>(reportService.getSkillReport(), HttpStatus.OK);
    }

    @GetMapping("/allocations")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR', 'DELIVERY_MANAGER')")
    public ResponseEntity<Map<String, List<String>>> allocationReport() {
        return new ResponseEntity<>(reportService.getAllocationReport(), HttpStatus.OK);
    }
}
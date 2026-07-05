package com.erasm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erasm.dto.UtilizationResponse;
import com.erasm.service.UtilizationService;

@RestController
@RequestMapping("/utilization")
public class UtilizationController {

    @Autowired
    private UtilizationService utilizationService;

    @GetMapping
    @PreAuthorize("hasAnyRole('DELIVERY_MANAGER', 'ADMIN', 'AUDITOR')")
    public ResponseEntity<List<UtilizationResponse>> getUtilization() {
        return new ResponseEntity<>(utilizationService.getUtilization(), HttpStatus.OK);
    }
}
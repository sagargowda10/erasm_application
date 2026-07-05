package com.erasm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erasm.dto.AllocationRequest;
import com.erasm.entity.Allocation;
import com.erasm.service.AllocationService;

@RestController
@RequestMapping("/allocations")
public class AllocationController {

    @Autowired
    private AllocationService allocationService;

    @PostMapping
    @PreAuthorize("hasRole('RESOURCE_MANAGER')")
    public ResponseEntity<Allocation> allocate(@RequestBody AllocationRequest request) {
        return new ResponseEntity<>(allocationService.allocate(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Allocation>> getAll() {
        return new ResponseEntity<>(allocationService.getAllAllocations(), HttpStatus.OK);
    }

    @PutMapping("/{id}/release")
    @PreAuthorize("hasRole('RESOURCE_MANAGER')")
    public ResponseEntity<Allocation> release(@PathVariable Integer id) {
        return new ResponseEntity<>(allocationService.release(id), HttpStatus.OK);
    }
}
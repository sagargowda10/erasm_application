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

import com.erasm.dto.ResourceRequestDto;
import com.erasm.entity.ResourceRequest;
import com.erasm.service.ResourceRequestService;

@RestController
@RequestMapping("/resource-requests")
public class ResourceRequestController {

    @Autowired
    private ResourceRequestService resourceRequestService;

    @PostMapping
    @PreAuthorize("hasRole('DELIVERY_MANAGER')")
    public ResponseEntity<ResourceRequest> create(@RequestBody ResourceRequestDto dto) {
        return new ResponseEntity<>(resourceRequestService.createRequest(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ResourceRequest>> getAll() {
        return new ResponseEntity<>(resourceRequestService.getAllRequests(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceRequest> getById(@PathVariable Integer id) {
        return new ResponseEntity<>(resourceRequestService.getRequestById(id), HttpStatus.OK);
    }

    // advance one step through the approval workflow
    @PutMapping("/{id}/advance")
    @PreAuthorize("hasRole('RESOURCE_MANAGER')")
    public ResponseEntity<ResourceRequest> advance(@PathVariable Integer id) {
        return new ResponseEntity<>(resourceRequestService.advanceStatus(id), HttpStatus.OK);
    }
}
package com.erasm.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erasm.dto.AllocationRequest;
import com.erasm.entity.Allocation;
import com.erasm.entity.Employee;
import com.erasm.entity.Project;
import com.erasm.exception.AllocationException;
import com.erasm.exception.ResourceNotFoundException;
import com.erasm.repository.AllocationRepository;
import com.erasm.repository.EmployeeRepository;
import com.erasm.repository.ProjectRepository;

@Service
public class AllocationService {

    private static final Logger logger = LoggerFactory.getLogger(AllocationService.class);

    @Autowired
    private AllocationRepository allocationRepository;

    
    @Autowired
    private AuditService auditService;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public Allocation allocate(AllocationRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + request.getEmployeeId()));
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + request.getProjectId()));

        int existingTotal = allocationRepository.findByEmployeeId(employee.getId()).stream()
                .mapToInt(Allocation::getAllocationPercentage)
                .sum();

        int newTotal = existingTotal + request.getAllocationPercentage();
        if (newTotal > 100) {
            logger.warn("Allocation rejected for employee {} - total would be {}%", employee.getId(), newTotal);
            throw new AllocationException("Total allocation cannot exceed 100%. Current: "
                    + existingTotal + "%, Requested: " + request.getAllocationPercentage() + "%");
        }

        Allocation allocation = new Allocation();
        allocation.setEmployee(employee);
        allocation.setProject(project);
        allocation.setAllocationPercentage(request.getAllocationPercentage());
        allocation.setStatus("ALLOCATED");

        logger.info("Allocating employee {} to project {} at {}%",
                employee.getId(), project.getId(), request.getAllocationPercentage());
        Allocation saved = allocationRepository.save(allocation);
        auditService.record("ALLOCATE", "Allocation");
        return saved;
    }

    public List<Allocation> getAllAllocations() {
        return allocationRepository.findAll();
    }

    public Allocation release(Integer id) {
        Allocation allocation = allocationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found: " + id));
        allocation.setStatus("RELEASED");
        allocation.setAllocationPercentage(0);
        logger.info("Released allocation id: {}", id);
        return allocationRepository.save(allocation);
    }
}
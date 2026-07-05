package com.erasm.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erasm.dto.UtilizationResponse;
import com.erasm.entity.Allocation;
import com.erasm.entity.Employee;
import com.erasm.repository.AllocationRepository;
import com.erasm.repository.EmployeeRepository;

@Service
public class UtilizationService {

    private static final Logger logger = LoggerFactory.getLogger(UtilizationService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AllocationRepository allocationRepository;

    public List<UtilizationResponse> getUtilization() {
        logger.info("Calculating utilization dashboard");
        List<UtilizationResponse> result = new ArrayList<>();

        for (Employee employee : employeeRepository.findAll()) {
            int billable = 0;
            for (Allocation allocation : allocationRepository.findByEmployeeId(employee.getId())) {
                if (allocation.getAllocationPercentage() != null) {
                    billable += allocation.getAllocationPercentage();
                }
            }
            if (billable > 100) {
                billable = 100;
            }
            int bench = 100 - billable;
            result.add(new UtilizationResponse(employee.getId(), employee.getName(), billable, bench));
        }
        return result;
    }
}
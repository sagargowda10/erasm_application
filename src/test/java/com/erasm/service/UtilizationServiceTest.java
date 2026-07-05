package com.erasm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.erasm.dto.UtilizationResponse;
import com.erasm.entity.Allocation;
import com.erasm.entity.Employee;
import com.erasm.repository.AllocationRepository;
import com.erasm.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class UtilizationServiceTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private AllocationRepository allocationRepository;

    @InjectMocks private UtilizationService utilizationService;

    private Employee employee(int id, String name) {
        Employee e = new Employee();
        e.setId(id);
        e.setName(name);
        return e;
    }

    private Allocation alloc(int pct) {
        Allocation a = new Allocation();
        a.setAllocationPercentage(pct);
        return a;
    }

    @Test
    void getUtilization_computesBillableAndBench() {
        Employee emp = employee(1, "Sagar");
        when(employeeRepository.findAll()).thenReturn(List.of(emp));
        // 60 + 20 = 80% billable -> 20% bench
        when(allocationRepository.findByEmployeeId(1)).thenReturn(List.of(alloc(60), alloc(20)));

        List<UtilizationResponse> result = utilizationService.getUtilization();

        assertEquals(1, result.size());
        assertEquals(80, result.get(0).getBillablePercentage());
        assertEquals(20, result.get(0).getBenchPercentage());
    }

    @Test
    void getUtilization_cappedAt100() {
        Employee emp = employee(1, "Sagar");
        when(employeeRepository.findAll()).thenReturn(List.of(emp));
        // 70 + 60 = 130 -> capped to 100 billable, 0 bench
        when(allocationRepository.findByEmployeeId(1)).thenReturn(List.of(alloc(70), alloc(60)));

        List<UtilizationResponse> result = utilizationService.getUtilization();

        assertEquals(100, result.get(0).getBillablePercentage());
        assertEquals(0, result.get(0).getBenchPercentage());
    }
}
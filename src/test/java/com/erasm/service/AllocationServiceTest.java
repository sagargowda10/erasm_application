package com.erasm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.erasm.dto.AllocationRequest;
import com.erasm.entity.Allocation;
import com.erasm.entity.Employee;
import com.erasm.entity.Project;
import com.erasm.exception.AllocationException;
import com.erasm.repository.AllocationRepository;
import com.erasm.repository.EmployeeRepository;
import com.erasm.repository.ProjectRepository;

@ExtendWith(MockitoExtension.class)
class AllocationServiceTest {

    @Mock private AllocationRepository allocationRepository;
    @Mock private EmployeeRepository employeeRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private AuditService auditService;

    @InjectMocks private AllocationService allocationService;

    private AllocationRequest request(int emp, int proj, int pct) {
        AllocationRequest r = new AllocationRequest();
        r.setEmployeeId(emp);
        r.setProjectId(proj);
        r.setAllocationPercentage(pct);
        return r;
    }

    // ---- within 100% -> allocation succeeds ----
    @Test
    void allocate_withinCap_succeeds() {
        Employee emp = new Employee();
        emp.setId(1);
        Project proj = new Project();
        proj.setId(1);

        when(employeeRepository.findById(1)).thenReturn(Optional.of(emp));
        when(projectRepository.findById(1)).thenReturn(Optional.of(proj));
        // existing allocations total 60%
        Allocation existing = new Allocation();
        existing.setAllocationPercentage(60);
        when(allocationRepository.findByEmployeeId(1)).thenReturn(List.of(existing));
        when(allocationRepository.save(any(Allocation.class))).thenAnswer(inv -> inv.getArgument(0));

        // request +40% -> total 100% -> allowed
        Allocation result = allocationService.allocate(request(1, 1, 40));

        assertNotNull(result);
        assertEquals(40, result.getAllocationPercentage());
        assertEquals("ALLOCATED", result.getStatus());
        verify(allocationRepository, times(1)).save(any(Allocation.class));
    }

    // ---- over 100% -> AllocationException, nothing saved ----
    @Test
    void allocate_overCap_throwsAllocationException() {
        Employee emp = new Employee();
        emp.setId(1);
        Project proj = new Project();
        proj.setId(1);

        when(employeeRepository.findById(1)).thenReturn(Optional.of(emp));
        when(projectRepository.findById(1)).thenReturn(Optional.of(proj));
        // existing allocations total 60%
        Allocation existing = new Allocation();
        existing.setAllocationPercentage(60);
        when(allocationRepository.findByEmployeeId(1)).thenReturn(List.of(existing));

        // request +50% -> total 110% -> rejected
        assertThrows(AllocationException.class, () -> allocationService.allocate(request(1, 1, 50)));

        // ensure nothing was persisted
        verify(allocationRepository, never()).save(any(Allocation.class));
    }

    // ---- release sets status RELEASED and percentage 0 ----
    @Test
    void release_setsStatusReleased() {
        Allocation existing = new Allocation();
        existing.setId(5);
        existing.setAllocationPercentage(60);
        existing.setStatus("ALLOCATED");

        when(allocationRepository.findById(5)).thenReturn(Optional.of(existing));
        when(allocationRepository.save(any(Allocation.class))).thenAnswer(inv -> inv.getArgument(0));

        Allocation result = allocationService.release(5);

        assertEquals("RELEASED", result.getStatus());
        assertEquals(0, result.getAllocationPercentage());
    }
}
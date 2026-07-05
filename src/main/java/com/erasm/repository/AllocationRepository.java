package com.erasm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.erasm.entity.Allocation;

public interface AllocationRepository extends JpaRepository<Allocation, Integer> {
    List<Allocation> findByEmployeeId(Integer employeeId);
    List<Allocation> findByProjectId(Integer projectId);
}
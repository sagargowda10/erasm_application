package com.erasm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erasm.entity.Certification;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Integer> {

    List<Certification> findByEmployeeId(Integer employeeId);
}
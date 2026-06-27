package com.erasm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erasm.entity.Certification;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Integer> {

}
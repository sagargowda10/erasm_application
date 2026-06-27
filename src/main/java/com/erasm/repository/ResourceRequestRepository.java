package com.erasm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erasm.entity.ResourceRequest;

@Repository
public interface ResourceRequestRepository extends JpaRepository<ResourceRequest, Integer> {

}
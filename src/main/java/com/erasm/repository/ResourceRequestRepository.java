package com.erasm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.erasm.entity.ResourceRequest;

public interface ResourceRequestRepository extends JpaRepository<ResourceRequest, Integer> {
    List<ResourceRequest> findByProjectId(Integer projectId);
}
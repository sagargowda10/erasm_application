package com.erasm.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erasm.dto.ResourceRequestDto;
import com.erasm.entity.Project;
import com.erasm.entity.ResourceRequest;
import com.erasm.exception.ResourceNotFoundException;
import com.erasm.repository.ProjectRepository;
import com.erasm.repository.ResourceRequestRepository;

@Service
public class ResourceRequestService {

    private static final Logger logger = LoggerFactory.getLogger(ResourceRequestService.class);

    // legal next-states for the approval workflow
    private static final Map<String, String> NEXT_STATE = Map.of(
            "DRAFT", "SUBMITTED",
            "SUBMITTED", "APPROVED",
            "APPROVED", "ALLOCATED",
            "ALLOCATED", "COMPLETED");

    @Autowired
    private ResourceRequestRepository resourceRequestRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ResourceRequest createRequest(ResourceRequestDto dto) {
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + dto.getProjectId()));

        ResourceRequest request = new ResourceRequest();
        request.setRequiredSkill(dto.getRequiredSkill());
        request.setRequiredCount(dto.getRequiredCount());
        request.setProject(project);
        request.setStatus("DRAFT");

        logger.info("Created resource request for project {} in DRAFT", project.getId());
        return resourceRequestRepository.save(request);
    }

    public List<ResourceRequest> getAllRequests() {
        return resourceRequestRepository.findAll();
    }

    public ResourceRequest getRequestById(Integer id) {
        return resourceRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource request not found: " + id));
    }

    // advances the request one legal step in the workflow
    public ResourceRequest advanceStatus(Integer id) {
        ResourceRequest request = getRequestById(id);
        String current = request.getStatus();
        String next = NEXT_STATE.get(current);
        if (next == null) {
            logger.warn("Cannot advance request {} - already at terminal state {}", id, current);
            throw new ResourceNotFoundException("Request is already at final state: " + current);
        }
        request.setStatus(next);
        logger.info("Resource request {} moved {} -> {}", id, current, next);
        return resourceRequestRepository.save(request);
    }
}
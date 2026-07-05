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

import com.erasm.dto.ResourceRequestDto;
import com.erasm.entity.Project;
import com.erasm.entity.ResourceRequest;
import com.erasm.exception.ResourceNotFoundException;
import com.erasm.repository.ProjectRepository;
import com.erasm.repository.ResourceRequestRepository;

@ExtendWith(MockitoExtension.class)
class ResourceRequestServiceTest {

    @Mock private ResourceRequestRepository resourceRequestRepository;
    @Mock private ProjectRepository projectRepository;

    @InjectMocks private ResourceRequestService resourceRequestService;

    private ResourceRequestDto dto(String skill, int count, int projectId) {
        ResourceRequestDto d = new ResourceRequestDto();
        d.setRequiredSkill(skill);
        d.setRequiredCount(count);
        d.setProjectId(projectId);
        return d;
    }

    private ResourceRequest requestWithStatus(int id, String status) {
        ResourceRequest r = new ResourceRequest();
        r.setId(id);
        r.setStatus(status);
        return r;
    }

    // ================= createRequest =================
    @Test
    void createRequest_success_startsInDraft() {
        Project project = new Project();
        project.setId(1);

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(resourceRequestRepository.save(any(ResourceRequest.class))).thenAnswer(inv -> inv.getArgument(0));

        ResourceRequest result = resourceRequestService.createRequest(dto("Java", 3, 1));

        assertNotNull(result);
        assertEquals("DRAFT", result.getStatus());
        assertEquals("Java", result.getRequiredSkill());
        verify(resourceRequestRepository, times(1)).save(any(ResourceRequest.class));
    }

    @Test
    void createRequest_projectNotFound_throws() {
        when(projectRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> resourceRequestService.createRequest(dto("Java", 3, 99)));
        verify(resourceRequestRepository, never()).save(any(ResourceRequest.class));
    }

    // ================= getAllRequests =================
    @Test
    void getAllRequests_returnsList() {
        when(resourceRequestRepository.findAll())
                .thenReturn(List.of(requestWithStatus(1, "DRAFT"), requestWithStatus(2, "SUBMITTED")));

        List<ResourceRequest> result = resourceRequestService.getAllRequests();

        assertEquals(2, result.size());
        verify(resourceRequestRepository, times(1)).findAll();
    }

    // ================= getRequestById =================
    @Test
    void getRequestById_notFound_throws() {
        when(resourceRequestRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> resourceRequestService.getRequestById(999));
    }

    // ================= advanceStatus =================
    @Test
    void advanceStatus_draftToSubmitted() {
        ResourceRequest req = requestWithStatus(1, "DRAFT");
        when(resourceRequestRepository.findById(1)).thenReturn(Optional.of(req));
        when(resourceRequestRepository.save(any(ResourceRequest.class))).thenAnswer(inv -> inv.getArgument(0));

        ResourceRequest result = resourceRequestService.advanceStatus(1);

        assertEquals("SUBMITTED", result.getStatus());
    }

    @Test
    void advanceStatus_fromCompleted_throws() {
        ResourceRequest req = requestWithStatus(1, "COMPLETED");
        when(resourceRequestRepository.findById(1)).thenReturn(Optional.of(req));

        // COMPLETED has no next state -> should throw
        assertThrows(ResourceNotFoundException.class,
                () -> resourceRequestService.advanceStatus(1));
        verify(resourceRequestRepository, never()).save(any(ResourceRequest.class));
    }
}
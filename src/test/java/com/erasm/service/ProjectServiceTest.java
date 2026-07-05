package com.erasm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.erasm.entity.Project;
import com.erasm.exception.ProjectNotFoundException;
import com.erasm.repository.ProjectRepository;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock private ProjectRepository projectRepository;
    @Mock private AuditService auditService;

    @InjectMocks private ProjectService projectService;

    @Test
    void createProject_savesAndReturnsProject() {
        Project p = new Project();
        p.setProjectName("Healthcare Portal");
        when(projectRepository.save(p)).thenReturn(p);

        Project result = projectService.createProject(p);

        assertNotNull(result);
        assertEquals("Healthcare Portal", result.getProjectName());
        verify(projectRepository, times(1)).save(p);
    }

    @Test
    void getProjectById_whenMissing_throwsProjectNotFound() {
        when(projectRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(ProjectNotFoundException.class,
                () -> projectService.getProjectById(999));
    }
}
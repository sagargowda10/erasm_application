package com.erasm.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.erasm.entity.Project;
import com.erasm.service.ProjectService;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    @Mock private ProjectService projectService;
    @InjectMocks private ProjectController controller;

    @Test
    void create_returnsCreated() {
        Project pr = new Project();
        when(projectService.createProject(any(Project.class))).thenReturn(pr);

        ResponseEntity<Project> res = controller.create(new Project());

        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        assertSame(pr, res.getBody());
    }

    @Test
    void getAll_returnsOk() {
        when(projectService.getAllProjects()).thenReturn(List.of(new Project()));

        ResponseEntity<List<Project>> res = controller.getAll();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }

    @Test
    void getById_returnsOk() {
        Project pr = new Project();
        when(projectService.getProjectById(1)).thenReturn(pr);

        ResponseEntity<Project> res = controller.getById(1);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertSame(pr, res.getBody());
    }

    @Test
    void update_returnsOk() {
        Project pr = new Project();
        when(projectService.updateProject(eq(1), any(Project.class))).thenReturn(pr);

        ResponseEntity<Project> res = controller.update(1, new Project());

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertSame(pr, res.getBody());
    }

    @Test
    void delete_returnsNoContent() {
        ResponseEntity<Void> res = controller.delete(1);

        assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
        verify(projectService, times(1)).deleteProject(1);
    }
}

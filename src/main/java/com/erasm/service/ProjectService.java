package com.erasm.service;

import java.util.List;
import com.erasm.exception.ProjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erasm.entity.Project;
import com.erasm.exception.ResourceNotFoundException;
import com.erasm.repository.ProjectRepository;

@Service
public class ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    
    @Autowired
    private AuditService auditService;
    
    @Autowired
    private ProjectRepository projectRepository;

    public Project createProject(Project project) {
        logger.info("Creating project: {}", project.getProjectName());
        Project saved = projectRepository.save(project);
        auditService.record("CREATE", "Project");
        return saved;
        
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Integer id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));
    }

    public Project updateProject(Integer id, Project updated) {
        Project project = getProjectById(id);
        project.setProjectName(updated.getProjectName());
        project.setClientName(updated.getClientName());
        project.setStartDate(updated.getStartDate());
        project.setEndDate(updated.getEndDate());
        project.setTechnologyStack(updated.getTechnologyStack());
        project.setBudget(updated.getBudget());
        logger.info("Updating project id: {}", id);
        return projectRepository.save(project);
    }

    public void deleteProject(Integer id) {
        Project project = getProjectById(id);
        projectRepository.delete(project);
        logger.info("Deleted project id: {}", id);
    }
}
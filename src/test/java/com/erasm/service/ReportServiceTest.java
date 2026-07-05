package com.erasm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.erasm.entity.Allocation;
import com.erasm.entity.Employee;
import com.erasm.entity.EmployeeSkill;
import com.erasm.entity.Project;
import com.erasm.entity.Skill;
import com.erasm.repository.AllocationRepository;
import com.erasm.repository.EmployeeSkillRepository;
import com.erasm.repository.ProjectRepository;
import com.erasm.repository.SkillRepository;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock private SkillRepository skillRepository;
    @Mock private EmployeeSkillRepository employeeSkillRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private AllocationRepository allocationRepository;

    @InjectMocks private ReportService reportService;

    @Test
    void getSkillReport_groupsEmployeesBySkill() {
        Skill java = new Skill();
        java.setId(1);
        java.setName("Java");

        Employee emp = new Employee();
        emp.setId(1);
        emp.setName("Sagar");

        EmployeeSkill es = new EmployeeSkill();
        es.setEmployee(emp);

        when(skillRepository.findAll()).thenReturn(List.of(java));
        when(employeeSkillRepository.findBySkillId(1)).thenReturn(List.of(es));

        Map<String, List<String>> report = reportService.getSkillReport();

        assertTrue(report.containsKey("Java"));
        assertEquals(List.of("Sagar"), report.get("Java"));
    }

    @Test
    void getAllocationReport_groupsEmployeesByProject() {
        Project project = new Project();
        project.setId(1);
        project.setProjectName("Healthcare Portal");

        Employee emp = new Employee();
        emp.setId(1);
        emp.setName("Sagar");

        Allocation alloc = new Allocation();
        alloc.setEmployee(emp);
        alloc.setAllocationPercentage(60);

        when(projectRepository.findAll()).thenReturn(List.of(project));
        when(allocationRepository.findByProjectId(1)).thenReturn(List.of(alloc));

        Map<String, List<String>> report = reportService.getAllocationReport();

        assertTrue(report.containsKey("Healthcare Portal"));
        assertEquals(List.of("Sagar (60%)"), report.get("Healthcare Portal"));
    }
}
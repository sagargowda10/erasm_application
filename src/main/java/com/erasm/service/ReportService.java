package com.erasm.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erasm.entity.Allocation;
import com.erasm.entity.EmployeeSkill;
import com.erasm.entity.Project;
import com.erasm.entity.Skill;
import com.erasm.repository.AllocationRepository;
import com.erasm.repository.EmployeeSkillRepository;
import com.erasm.repository.ProjectRepository;
import com.erasm.repository.SkillRepository;

@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private EmployeeSkillRepository employeeSkillRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AllocationRepository allocationRepository;

    // Skill Report: employees grouped by skill
    public Map<String, List<String>> getSkillReport() {
        logger.info("Generating skill report");
        Map<String, List<String>> report = new LinkedHashMap<>();
        for (Skill skill : skillRepository.findAll()) {
            List<String> employees = new ArrayList<>();
            for (EmployeeSkill es : employeeSkillRepository.findBySkillId(skill.getId())) {
                employees.add(es.getEmployee().getName());
            }
            report.put(skill.getName(), employees);
        }
        return report;
    }

    // Allocation Report: employees assigned to each project
    public Map<String, List<String>> getAllocationReport() {
        logger.info("Generating allocation report");
        Map<String, List<String>> report = new LinkedHashMap<>();
        for (Project project : projectRepository.findAll()) {
            List<String> employees = new ArrayList<>();
            for (Allocation allocation : allocationRepository.findByProjectId(project.getId())) {
                employees.add(allocation.getEmployee().getName() + " (" + allocation.getAllocationPercentage() + "%)");
            }
            report.put(project.getProjectName(), employees);
        }
        return report;
    }
}
package com.erasm.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erasm.dto.EmployeeSkillRequest;
import com.erasm.entity.Employee;
import com.erasm.entity.EmployeeSkill;
import com.erasm.entity.Skill;
import com.erasm.exception.ResourceNotFoundException;
import com.erasm.repository.EmployeeRepository;
import com.erasm.repository.EmployeeSkillRepository;
import com.erasm.repository.SkillRepository;

@Service
public class EmployeeSkillService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeSkillService.class);

    @Autowired
    private EmployeeSkillRepository employeeSkillRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SkillRepository skillRepository;

    public EmployeeSkill addEmployeeSkill(EmployeeSkillRequest request) {
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found: " + request.getEmployeeId()));
        Skill skill = skillRepository.findById(request.getSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found: " + request.getSkillId()));

        EmployeeSkill es = new EmployeeSkill();
        es.setEmployee(employee);
        es.setSkill(skill);
        es.setSkillLevel(request.getSkillLevel());
        es.setExperience(request.getExperience());

        logger.info("Adding skill {} to employee {}", skill.getId(), employee.getId());
        return employeeSkillRepository.save(es);
    }

    public List<EmployeeSkill> getAllEmployeeSkills() {
        return employeeSkillRepository.findAll();
    }

    public EmployeeSkill updateEmployeeSkill(Integer id, EmployeeSkillRequest request) {
        EmployeeSkill es = employeeSkillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EmployeeSkill not found: " + id));
        es.setSkillLevel(request.getSkillLevel());
        es.setExperience(request.getExperience());
        logger.info("Updated employee-skill id: {}", id);
        return employeeSkillRepository.save(es);
    }
}
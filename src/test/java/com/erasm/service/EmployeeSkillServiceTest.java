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

import com.erasm.dto.EmployeeSkillRequest;
import com.erasm.entity.Employee;
import com.erasm.entity.EmployeeSkill;
import com.erasm.entity.Skill;
import com.erasm.exception.ResourceNotFoundException;
import com.erasm.repository.EmployeeRepository;
import com.erasm.repository.EmployeeSkillRepository;
import com.erasm.repository.SkillRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeSkillServiceTest {

    @Mock private EmployeeSkillRepository employeeSkillRepository;
    @Mock private EmployeeRepository employeeRepository;
    @Mock private SkillRepository skillRepository;

    @InjectMocks private EmployeeSkillService employeeSkillService;

    private EmployeeSkillRequest request(int empId, int skillId, String level, int exp) {
        EmployeeSkillRequest r = new EmployeeSkillRequest();
        r.setEmployeeId(empId);
        r.setSkillId(skillId);
        r.setSkillLevel(level);
        r.setExperience(exp);
        return r;
    }

    @Test
    void addEmployeeSkill_success() {
        Employee e = new Employee(); e.setId(1);
        Skill s = new Skill(); s.setId(2);
        when(employeeRepository.findById(1)).thenReturn(Optional.of(e));
        when(skillRepository.findById(2)).thenReturn(Optional.of(s));
        when(employeeSkillRepository.save(any(EmployeeSkill.class))).thenAnswer(inv -> inv.getArgument(0));

        EmployeeSkill result = employeeSkillService.addEmployeeSkill(request(1, 2, "Advanced", 4));

        assertEquals("Advanced", result.getSkillLevel());
        assertEquals(4, result.getExperience());
        verify(employeeSkillRepository, times(1)).save(any(EmployeeSkill.class));
    }

    @Test
    void addEmployeeSkill_employeeNotFound_throws() {
        when(employeeRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeSkillService.addEmployeeSkill(request(99, 2, "Beginner", 1)));
    }

    @Test
    void addEmployeeSkill_skillNotFound_throws() {
        Employee e = new Employee(); e.setId(1);
        when(employeeRepository.findById(1)).thenReturn(Optional.of(e));
        when(skillRepository.findById(88)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeSkillService.addEmployeeSkill(request(1, 88, "Beginner", 1)));
    }

    @Test
    void getAllEmployeeSkills_returnsList() {
        when(employeeSkillRepository.findAll()).thenReturn(List.of(new EmployeeSkill(), new EmployeeSkill()));

        assertEquals(2, employeeSkillService.getAllEmployeeSkills().size());
    }

    @Test
    void updateEmployeeSkill_success() {
        EmployeeSkill existing = new EmployeeSkill();
        existing.setId(1);
        when(employeeSkillRepository.findById(1)).thenReturn(Optional.of(existing));
        when(employeeSkillRepository.save(any(EmployeeSkill.class))).thenAnswer(inv -> inv.getArgument(0));

        EmployeeSkill result = employeeSkillService.updateEmployeeSkill(1, request(1, 2, "Intermediate", 3));

        assertEquals("Intermediate", result.getSkillLevel());
        assertEquals(3, result.getExperience());
    }

    @Test
    void updateEmployeeSkill_notFound_throws() {
        when(employeeSkillRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeSkillService.updateEmployeeSkill(999, request(1, 2, "x", 1)));
    }
}
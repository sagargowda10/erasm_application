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

import com.erasm.dto.EmployeeSkillRequest;
import com.erasm.entity.EmployeeSkill;
import com.erasm.service.EmployeeSkillService;

@ExtendWith(MockitoExtension.class)
class EmployeeSkillControllerTest {

    @Mock private EmployeeSkillService employeeSkillService;
    @InjectMocks private EmployeeSkillController controller;

    @Test
    void add_returnsCreated() {
        EmployeeSkill es = new EmployeeSkill();
        when(employeeSkillService.addEmployeeSkill(any(EmployeeSkillRequest.class))).thenReturn(es);

        ResponseEntity<EmployeeSkill> res = controller.add(new EmployeeSkillRequest());

        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        assertSame(es, res.getBody());
    }

    @Test
    void getAll_returnsOk() {
        when(employeeSkillService.getAllEmployeeSkills()).thenReturn(List.of(new EmployeeSkill()));

        ResponseEntity<List<EmployeeSkill>> res = controller.getAll();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }

    @Test
    void update_returnsOk() {
        EmployeeSkill es = new EmployeeSkill();
        when(employeeSkillService.updateEmployeeSkill(eq(1), any(EmployeeSkillRequest.class))).thenReturn(es);

        ResponseEntity<EmployeeSkill> res = controller.update(1, new EmployeeSkillRequest());

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertSame(es, res.getBody());
    }
}

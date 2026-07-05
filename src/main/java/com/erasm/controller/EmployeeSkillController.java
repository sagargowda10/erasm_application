package com.erasm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erasm.dto.EmployeeSkillRequest;
import com.erasm.entity.EmployeeSkill;
import com.erasm.service.EmployeeSkillService;

@RestController
@RequestMapping("/employee-skills")
public class EmployeeSkillController {

    @Autowired
    private EmployeeSkillService employeeSkillService;

    @PostMapping
    public ResponseEntity<EmployeeSkill> add(@RequestBody EmployeeSkillRequest request) {
        return new ResponseEntity<>(employeeSkillService.addEmployeeSkill(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeSkill>> getAll() {
        return new ResponseEntity<>(employeeSkillService.getAllEmployeeSkills(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeSkill> update(@PathVariable Integer id,
            @RequestBody EmployeeSkillRequest request) {
        return new ResponseEntity<>(employeeSkillService.updateEmployeeSkill(id, request), HttpStatus.OK);
    }
}
package com.erasm.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erasm.dto.EmployeeRequest;
import com.erasm.entity.Employee;
import com.erasm.entity.User;
import com.erasm.exception.ResourceNotFoundException;
import com.erasm.repository.EmployeeRepository;
import com.erasm.repository.UserRepository;

@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    public Employee createEmployee(EmployeeRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUserId()));

        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setDesignation(request.getDesignation());
        employee.setExperience(request.getExperience());
        employee.setUser(user);

        logger.info("Creating employee: {}", request.getName());
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    public Employee updateEmployee(Integer id, EmployeeRequest request) {
        Employee employee = getEmployeeById(id);
        employee.setName(request.getName());
        employee.setDesignation(request.getDesignation());
        employee.setExperience(request.getExperience());
        logger.info("Updated employee id: {}", id);
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Integer id) {
        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);
        logger.warn("Deleted employee id: {}", id);
    }
}
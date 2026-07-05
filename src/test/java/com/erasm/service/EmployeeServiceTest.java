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

import com.erasm.dto.EmployeeRequest;
import com.erasm.entity.Employee;
import com.erasm.entity.User;
import com.erasm.exception.ResourceNotFoundException;
import com.erasm.repository.EmployeeRepository;
import com.erasm.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private EmployeeService employeeService;

    private EmployeeRequest request(String name, String desig, int exp, int userId) {
        EmployeeRequest r = new EmployeeRequest();
        r.setName(name);
        r.setDesignation(desig);
        r.setExperience(exp);
        r.setUserId(userId);
        return r;
    }

    private Employee employee(int id, String name) {
        Employee e = new Employee();
        e.setId(id);
        e.setName(name);
        return e;
    }

    // ================= createEmployee =================
    @Test
    void createEmployee_success() {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        Employee result = employeeService.createEmployee(request("Sagar", "Developer", 3, 1));

        assertNotNull(result);
        assertEquals("Sagar", result.getName());
        assertEquals("Developer", result.getDesignation());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void createEmployee_userNotFound_throws() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.createEmployee(request("X", "Dev", 1, 99)));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // ================= getAllEmployees =================
    @Test
    void getAllEmployees_returnsList() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee(1, "A"), employee(2, "B")));

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findAll();
    }

    // ================= getEmployeeById =================
    @Test
    void getEmployeeById_success() {
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee(1, "Sagar")));

        Employee result = employeeService.getEmployeeById(1);

        assertNotNull(result);
        assertEquals("Sagar", result.getName());
    }

    @Test
    void getEmployeeById_notFound_throws() {
        when(employeeRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById(999));
    }

    // ================= updateEmployee =================
    @Test
    void updateEmployee_success() {
        Employee existing = employee(1, "Old Name");
        when(employeeRepository.findById(1)).thenReturn(Optional.of(existing));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(inv -> inv.getArgument(0));

        Employee result = employeeService.updateEmployee(1, request("New Name", "Senior Developer", 5, 1));

        assertEquals("New Name", result.getName());
        assertEquals("Senior Developer", result.getDesignation());
        verify(employeeRepository, times(1)).save(existing);
    }

    // ================= deleteEmployee =================
    @Test
    void deleteEmployee_success() {
        Employee existing = employee(1, "Sagar");
        when(employeeRepository.findById(1)).thenReturn(Optional.of(existing));

        employeeService.deleteEmployee(1);

        verify(employeeRepository, times(1)).delete(existing);
    }
}
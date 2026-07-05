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

import com.erasm.dto.EmployeeRequest;
import com.erasm.entity.Employee;
import com.erasm.service.EmployeeService;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock private EmployeeService employeeService;
    @InjectMocks private EmployeeController controller;

    @Test
    void create_returnsCreated() {
        Employee e = new Employee();
        when(employeeService.createEmployee(any(EmployeeRequest.class))).thenReturn(e);

        ResponseEntity<Employee> res = controller.create(new EmployeeRequest());

        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        assertSame(e, res.getBody());
    }

    @Test
    void getAll_returnsOk() {
        when(employeeService.getAllEmployees()).thenReturn(List.of(new Employee()));

        ResponseEntity<List<Employee>> res = controller.getAll();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }

    @Test
    void getById_returnsOk() {
        Employee e = new Employee();
        when(employeeService.getEmployeeById(1)).thenReturn(e);

        ResponseEntity<Employee> res = controller.getById(1);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertSame(e, res.getBody());
    }

    @Test
    void update_returnsOk() {
        Employee e = new Employee();
        when(employeeService.updateEmployee(eq(1), any(EmployeeRequest.class))).thenReturn(e);

        ResponseEntity<Employee> res = controller.update(1, new EmployeeRequest());

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertSame(e, res.getBody());
    }

    @Test
    void delete_returnsNoContent() {
        ResponseEntity<Void> res = controller.delete(1);

        assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
        verify(employeeService, times(1)).deleteEmployee(1);
    }
}

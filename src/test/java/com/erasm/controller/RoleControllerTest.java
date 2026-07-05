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

import com.erasm.entity.Role;
import com.erasm.service.RoleService;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    @Mock private RoleService roleService;
    @InjectMocks private RoleController controller;

    @Test
    void add_returnsCreated() {
        Role r = new Role();
        when(roleService.addRole(any(Role.class))).thenReturn(r);

        ResponseEntity<Role> res = controller.add(new Role());

        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        assertSame(r, res.getBody());
    }

    @Test
    void getAll_returnsOk() {
        when(roleService.getAllRoles()).thenReturn(List.of(new Role()));

        ResponseEntity<List<Role>> res = controller.getAll();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }

    @Test
    void update_returnsOk() {
        Role r = new Role();
        when(roleService.updateRole(eq(1), any(Role.class))).thenReturn(r);

        ResponseEntity<Role> res = controller.update(1, new Role());

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertSame(r, res.getBody());
    }

    @Test
    void delete_returnsNoContent() {
        ResponseEntity<Void> res = controller.delete(1);

        assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
        verify(roleService, times(1)).deleteRole(1);
    }
}

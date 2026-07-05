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

import com.erasm.entity.Role;
import com.erasm.exception.ResourceNotFoundException;
import com.erasm.repository.RoleRepository;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock private RoleRepository roleRepository;
    @Mock private AuditService auditService;

    @InjectMocks private RoleService roleService;

    private Role role(int id, String name) {
        Role r = new Role();
        r.setId(id);
        r.setName(name);
        return r;
    }

    @Test
    void addRole_success() {
        when(roleRepository.save(any(Role.class))).thenAnswer(inv -> inv.getArgument(0));

        Role result = roleService.addRole(role(0, "ADMIN"));

        assertEquals("ADMIN", result.getName());
        verify(roleRepository, times(1)).save(any(Role.class));
        verify(auditService, times(1)).record("CREATE", "Role");
    }

    @Test
    void getAllRoles_returnsList() {
        when(roleRepository.findAll()).thenReturn(List.of(role(1, "ADMIN"), role(2, "EMPLOYEE")));

        assertEquals(2, roleService.getAllRoles().size());
    }

    @Test
    void getRoleById_success() {
        when(roleRepository.findById(1)).thenReturn(Optional.of(role(1, "ADMIN")));

        assertEquals("ADMIN", roleService.getRoleById(1).getName());
    }

    @Test
    void getRoleById_notFound_throws() {
        when(roleRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.getRoleById(99));
    }

    @Test
    void updateRole_success() {
        Role existing = role(1, "OLD");
        when(roleRepository.findById(1)).thenReturn(Optional.of(existing));
        when(roleRepository.save(any(Role.class))).thenAnswer(inv -> inv.getArgument(0));

        Role result = roleService.updateRole(1, role(0, "RESOURCE_MANAGER"));

        assertEquals("RESOURCE_MANAGER", result.getName());
        verify(auditService, times(1)).record("UPDATE", "Role");
    }

    @Test
    void deleteRole_success() {
        Role existing = role(1, "EMPLOYEE");
        when(roleRepository.findById(1)).thenReturn(Optional.of(existing));

        roleService.deleteRole(1);

        verify(roleRepository, times(1)).delete(existing);
        verify(auditService, times(1)).record("DELETE", "Role");
    }
}
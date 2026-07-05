package com.erasm.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erasm.entity.Role;
import com.erasm.exception.ResourceNotFoundException;
import com.erasm.repository.RoleRepository;

@Service
public class RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuditService auditService;

    public Role addRole(Role role) {
        Role saved = roleRepository.save(role);
        auditService.record("CREATE", "Role");
        logger.info("Created role: {}", saved.getName());
        return saved;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
    }

    public Role updateRole(Integer id, Role updated) {
        Role role = getRoleById(id);      // fetch-or-throw
        role.setName(updated.getName());  // only the name is mutable
        auditService.record("UPDATE", "Role");
        logger.info("Updated role id: {}", id);
        return roleRepository.save(role);
    }

    public void deleteRole(Integer id) {
        Role role = getRoleById(id);
        roleRepository.delete(role);
        auditService.record("DELETE", "Role");
        logger.warn("Deleted role id: {}", id);
    }
}
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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.erasm.dto.UserRequest;
import com.erasm.dto.UserResponse;
import com.erasm.entity.Role;
import com.erasm.entity.User;
import com.erasm.exception.ResourceNotFoundException;
import com.erasm.exception.UserNotFoundException;
import com.erasm.mapper.UserMapper;
import com.erasm.repository.RoleRepository;
import com.erasm.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UserMapper userMapper;

    @InjectMocks private UserService userService;

    // ---- helpers to build test data ----
    private UserRequest request(String email, String pwd, String role) {
        UserRequest r = new UserRequest();
        r.setEmail(email);
        r.setPassword(pwd);
        r.setRoleName(role);
        return r;
    }

    private Role role(int id, String name) {
        Role r = new Role();
        r.setId(id);
        r.setName(name);
        return r;
    }

    private User user(int id, String email, Role role) {
        User u = new User();
        u.setId(id);
        u.setEmail(email);
        u.setRole(role);
        return u;
    }

    private UserResponse response(int id, String email, String roleName) {
        UserResponse resp = new UserResponse();
        resp.setId(id);
        resp.setEmail(email);
        resp.setRoleName(roleName);
        return resp;
    }

    // ================= register =================
    @Test
    void registerUser_success() {
        Role admin = role(1, "ADMIN");
        User saved = user(1, "test@erasm.com", admin);

        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(admin));
        when(passwordEncoder.encode("password123")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenReturn(saved);
        when(userMapper.toResponse(saved)).thenReturn(response(1, "test@erasm.com", "ADMIN"));

        UserResponse result = userService.registerUser(request("test@erasm.com", "password123", "ADMIN"));

        assertNotNull(result);
        assertEquals("test@erasm.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_roleNotFound_throws() {
        when(roleRepository.findByName("BADROLE")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.registerUser(request("x@erasm.com", "password123", "BADROLE")));
        verify(userRepository, never()).save(any(User.class));
    }

    // ================= getAllUsers =================
    @Test
    void getAllUsers_returnsList() {
        Role admin = role(1, "ADMIN");
        User u1 = user(1, "a@erasm.com", admin);
        User u2 = user(2, "b@erasm.com", admin);

        when(userRepository.findAll()).thenReturn(List.of(u1, u2));
        when(userMapper.toResponse(u1)).thenReturn(response(1, "a@erasm.com", "ADMIN"));
        when(userMapper.toResponse(u2)).thenReturn(response(2, "b@erasm.com", "ADMIN"));

        List<UserResponse> result = userService.getAllUsers();

        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }

    // ================= getUserById =================
    @Test
    void getUserById_success() {
        Role admin = role(1, "ADMIN");
        User u = user(1, "a@erasm.com", admin);

        when(userRepository.findById(1)).thenReturn(Optional.of(u));
        when(userMapper.toResponse(u)).thenReturn(response(1, "a@erasm.com", "ADMIN"));

        UserResponse result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals("a@erasm.com", result.getEmail());
    }

    @Test
    void getUserById_notFound_throws() {
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(999));
    }

    // ================= updateUser =================
    @Test
    void updateUser_success() {
        Role dm = role(2, "DELIVERY_MANAGER");
        User existing = user(1, "old@erasm.com", role(1, "ADMIN"));
        User saved = user(1, "new@erasm.com", dm);

        when(userRepository.findById(1)).thenReturn(Optional.of(existing));
        when(roleRepository.findByName("DELIVERY_MANAGER")).thenReturn(Optional.of(dm));
        when(userRepository.save(any(User.class))).thenReturn(saved);
        when(userMapper.toResponse(saved)).thenReturn(response(1, "new@erasm.com", "DELIVERY_MANAGER"));

        UserResponse result = userService.updateUser(1, request("new@erasm.com", "x", "DELIVERY_MANAGER"));

        assertEquals("new@erasm.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    // ================= deleteUser =================
    @Test
    void deleteUser_success() {
        Role admin = role(1, "ADMIN");
        User existing = user(1, "a@erasm.com", admin);

        when(userRepository.findById(1)).thenReturn(Optional.of(existing));

        userService.deleteUser(1);

        verify(userRepository, times(1)).delete(existing);
    }

    // ================= changePassword =================
    @Test
    void changePassword_success() {
        Role admin = role(1, "ADMIN");
        User existing = user(1, "a@erasm.com", admin);
        existing.setPassword("hashedOld");

        when(userRepository.findById(1)).thenReturn(Optional.of(existing));
        when(passwordEncoder.matches("oldpass", "hashedOld")).thenReturn(true);
        when(passwordEncoder.encode("newpass123")).thenReturn("hashedNew");

        userService.changePassword(1, "oldpass", "newpass123");

        verify(userRepository, times(1)).save(existing);
    }

    @Test
    void changePassword_wrongOldPassword_throws() {
        Role admin = role(1, "ADMIN");
        User existing = user(1, "a@erasm.com", admin);
        existing.setPassword("hashedOld");

        when(userRepository.findById(1)).thenReturn(Optional.of(existing));
        when(passwordEncoder.matches("wrong", "hashedOld")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> userService.changePassword(1, "wrong", "newpass123"));
        verify(userRepository, never()).save(any(User.class));
    }
}
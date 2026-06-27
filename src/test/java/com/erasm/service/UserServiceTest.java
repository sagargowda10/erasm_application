package com.erasm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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
import com.erasm.mapper.UserMapper;
import com.erasm.repository.RoleRepository;
import com.erasm.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_success() {
        UserRequest request = new UserRequest();
        request.setEmail("test@erasm.com");
        request.setPassword("password123");
        request.setRoleName("ADMIN");

        Role role = new Role();
        role.setId(1);
        role.setName("ADMIN");

        User savedUser = new User();
        savedUser.setId(10);
        savedUser.setEmail("test@erasm.com");
        savedUser.setRole(role);

        UserResponse mappedResponse = new UserResponse();
        mappedResponse.setId(10);
        mappedResponse.setEmail("test@erasm.com");
        mappedResponse.setRoleName("ADMIN");

        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toResponse(savedUser)).thenReturn(mappedResponse);

        UserResponse result = userService.registerUser(request);

        assertEquals("test@erasm.com", result.getEmail());
        assertEquals("ADMIN", result.getRoleName());
        assertEquals("User registered successfully", result.getMessage());
    }

    @Test
    void registerUser_roleNotFound_throwsException() {
        UserRequest request = new UserRequest();
        request.setEmail("test@erasm.com");
        request.setPassword("password123");
        request.setRoleName("UNKNOWN");

        when(roleRepository.findByName("UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.registerUser(request));
    }
}
package com.erasm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.erasm.dto.UserRequest;
import com.erasm.dto.UserResponse;
import com.erasm.entity.Role;
import com.erasm.entity.User;
import com.erasm.mapper.UserMapper;
import com.erasm.repository.RoleRepository;
import com.erasm.repository.UserRepository;

import com.erasm.exception.ResourceNotFoundException;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    public UserResponse registerUser(UserRequest request) {

        logger.info("Registering new user with email: {}", request.getEmail());

        Role role = roleRepository.findByName(request.getRoleName())
        		.orElseThrow(() -> {
                    logger.warn("Registration failed - role not found: {}", request.getRoleName());
                    return new ResourceNotFoundException("Role not found: " + request.getRoleName());
                });

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);

        User savedUser = userRepository.save(user);

        logger.info("User registered successfully with id: {}", savedUser.getId());

        UserResponse response = userMapper.toResponse(savedUser);
        response.setMessage("User registered successfully");
        return response;
    }
}
package com.erasm.service;

import java.util.List;
import com.erasm.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.erasm.dto.UserRequest;
import com.erasm.dto.UserResponse;
import com.erasm.entity.Role;
import com.erasm.entity.User;
import com.erasm.exception.ResourceNotFoundException;
import com.erasm.mapper.UserMapper;
import com.erasm.repository.RoleRepository;
import com.erasm.repository.UserRepository;

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

    // private helper: returns the entity for internal modify/save operations
    private User findUserEntity(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponse getUserById(Integer id) {
        return userMapper.toResponse(findUserEntity(id));
    }

    public UserResponse updateUser(Integer id, UserRequest request) {
        User user = findUserEntity(id);
        user.setEmail(request.getEmail());

        Role role = roleRepository.findByName(request.getRoleName())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + request.getRoleName()));
        user.setRole(role);

        User saved = userRepository.save(user);
        logger.info("Updated user id: {}", id);

        UserResponse response = userMapper.toResponse(saved);
        response.setMessage("User updated successfully");
        return response;
    }

    public void deleteUser(Integer id) {
        User user = findUserEntity(id);
        userRepository.delete(user);
        logger.warn("Deleted user id: {}", id);
    }

    public void changePassword(Integer id, String oldPassword, String newPassword) {
        User user = findUserEntity(id);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            logger.warn("Change password failed - wrong old password for user id: {}", id);
            throw new ResourceNotFoundException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        logger.info("Password changed for user id: {}", id);
    }
}
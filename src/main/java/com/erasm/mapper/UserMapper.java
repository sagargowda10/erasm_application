package com.erasm.mapper;

import org.springframework.stereotype.Component;

import com.erasm.dto.UserResponse;
import com.erasm.entity.User;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setRoleName(user.getRole().getName());

        return response;
    }
}
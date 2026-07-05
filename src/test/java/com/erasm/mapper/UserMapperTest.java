package com.erasm.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.erasm.dto.UserResponse;
import com.erasm.entity.Role;
import com.erasm.entity.User;

class UserMapperTest {

    private final UserMapper mapper = new UserMapper();

    @Test
    void toResponse_mapsAllFields() {
        Role role = new Role();
        role.setName("ADMIN");

        User user = new User();
        user.setId(7);
        user.setEmail("sagar@erasm.com");
        user.setRole(role);

        UserResponse response = mapper.toResponse(user);

        assertEquals(7, response.getId());
        assertEquals("sagar@erasm.com", response.getEmail());
        assertEquals("ADMIN", response.getRoleName());
    }
}

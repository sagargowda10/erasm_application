package com.erasm.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import com.erasm.dto.LoginRequest;
import com.erasm.dto.UserRequest;
import com.erasm.dto.UserResponse;
import com.erasm.service.UserService;
import com.erasm.utility.JWTUtil;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock private AuthenticationManager authenticationManager;
    @Mock private JWTUtil jwtUtil;
    @Mock private UserService userService;

    @InjectMocks private AuthController controller;

    @Test
    void register_returnsCreated() {
        UserResponse resp = new UserResponse();
        when(userService.registerUser(any(UserRequest.class))).thenReturn(resp);

        ResponseEntity<UserResponse> res = controller.register(new UserRequest());

        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        assertSame(resp, res.getBody());
    }

    @Test
    void login_returnsTokenOk() {
        LoginRequest req = new LoginRequest();
        req.setEmail("a@b.com");
        req.setPassword("password1");
        when(jwtUtil.generateToken("a@b.com")).thenReturn("jwt-token");

        ResponseEntity<String> res = controller.login(req);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals("jwt-token", res.getBody());
        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    void logout_returnsOk() {
        ResponseEntity<String> res = controller.logout();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertNotNull(res.getBody());
    }
}

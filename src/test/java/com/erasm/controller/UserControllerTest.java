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

import com.erasm.dto.ChangePasswordRequest;
import com.erasm.dto.UserRequest;
import com.erasm.dto.UserResponse;
import com.erasm.service.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock private UserService userService;
    @InjectMocks private UserController controller;

    @Test
    void getAllUsers_returnsOk() {
        when(userService.getAllUsers()).thenReturn(List.of(new UserResponse()));

        ResponseEntity<List<UserResponse>> res = controller.getAllUsers();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }

    @Test
    void getUserById_returnsOk() {
        UserResponse u = new UserResponse();
        when(userService.getUserById(1)).thenReturn(u);

        ResponseEntity<UserResponse> res = controller.getUserById(1);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertSame(u, res.getBody());
    }

    @Test
    void updateUser_returnsOk() {
        UserResponse u = new UserResponse();
        when(userService.updateUser(eq(1), any(UserRequest.class))).thenReturn(u);

        ResponseEntity<UserResponse> res = controller.updateUser(1, new UserRequest());

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertSame(u, res.getBody());
    }

    @Test
    void deleteUser_returnsNoContent() {
        ResponseEntity<Void> res = controller.deleteUser(1);

        assertEquals(HttpStatus.NO_CONTENT, res.getStatusCode());
        verify(userService, times(1)).deleteUser(1);
    }

    @Test
    void changePassword_returnsOk() {
        ChangePasswordRequest req = new ChangePasswordRequest();
        req.setOldPassword("oldpass12");
        req.setNewPassword("newpass12");

        ResponseEntity<String> res = controller.changePassword(1, req);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        verify(userService, times(1)).changePassword(1, "oldpass12", "newpass12");
    }
}

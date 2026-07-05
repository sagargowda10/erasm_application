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

import com.erasm.dto.UtilizationResponse;
import com.erasm.service.UtilizationService;

@ExtendWith(MockitoExtension.class)
class UtilizationControllerTest {

    @Mock private UtilizationService utilizationService;
    @InjectMocks private UtilizationController controller;

    @Test
    void getUtilization_returnsOk() {
        UtilizationResponse u = new UtilizationResponse(1, "Sagar", 80, 20);
        when(utilizationService.getUtilization()).thenReturn(List.of(u));

        ResponseEntity<List<UtilizationResponse>> res = controller.getUtilization();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }
}

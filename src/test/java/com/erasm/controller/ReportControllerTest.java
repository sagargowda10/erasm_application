package com.erasm.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.erasm.service.ReportService;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Mock private ReportService reportService;
    @InjectMocks private ReportController controller;

    @Test
    void skillReport_returnsOk() {
        when(reportService.getSkillReport()).thenReturn(Map.of("Java", List.of("Sagar")));

        ResponseEntity<Map<String, List<String>>> res = controller.skillReport();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }

    @Test
    void allocationReport_returnsOk() {
        when(reportService.getAllocationReport()).thenReturn(Map.of("Healthcare Portal", List.of("Sagar")));

        ResponseEntity<Map<String, List<String>>> res = controller.allocationReport();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }
}

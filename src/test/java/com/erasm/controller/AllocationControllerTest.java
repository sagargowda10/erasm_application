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

import com.erasm.dto.AllocationRequest;
import com.erasm.entity.Allocation;
import com.erasm.service.AllocationService;

@ExtendWith(MockitoExtension.class)
class AllocationControllerTest {

    @Mock private AllocationService allocationService;
    @InjectMocks private AllocationController controller;

    @Test
    void allocate_returnsCreated() {
        Allocation a = new Allocation();
        when(allocationService.allocate(any(AllocationRequest.class))).thenReturn(a);

        ResponseEntity<Allocation> res = controller.allocate(new AllocationRequest());

        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        assertSame(a, res.getBody());
    }

    @Test
    void getAll_returnsOk() {
        when(allocationService.getAllAllocations()).thenReturn(List.of(new Allocation()));

        ResponseEntity<List<Allocation>> res = controller.getAll();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }

    @Test
    void release_returnsOk() {
        Allocation a = new Allocation();
        when(allocationService.release(1)).thenReturn(a);

        ResponseEntity<Allocation> res = controller.release(1);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertSame(a, res.getBody());
    }
}

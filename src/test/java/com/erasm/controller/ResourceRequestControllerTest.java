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

import com.erasm.dto.ResourceRequestDto;
import com.erasm.entity.ResourceRequest;
import com.erasm.service.ResourceRequestService;

@ExtendWith(MockitoExtension.class)
class ResourceRequestControllerTest {

    @Mock private ResourceRequestService resourceRequestService;
    @InjectMocks private ResourceRequestController controller;

    @Test
    void create_returnsCreated() {
        ResourceRequest r = new ResourceRequest();
        when(resourceRequestService.createRequest(any(ResourceRequestDto.class))).thenReturn(r);

        ResponseEntity<ResourceRequest> res = controller.create(new ResourceRequestDto());

        assertEquals(HttpStatus.CREATED, res.getStatusCode());
        assertSame(r, res.getBody());
    }

    @Test
    void getAll_returnsOk() {
        when(resourceRequestService.getAllRequests()).thenReturn(List.of(new ResourceRequest()));

        ResponseEntity<List<ResourceRequest>> res = controller.getAll();

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals(1, res.getBody().size());
    }

    @Test
    void getById_returnsOk() {
        ResourceRequest r = new ResourceRequest();
        when(resourceRequestService.getRequestById(1)).thenReturn(r);

        ResponseEntity<ResourceRequest> res = controller.getById(1);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertSame(r, res.getBody());
    }

    @Test
    void advance_returnsOk() {
        ResourceRequest r = new ResourceRequest();
        when(resourceRequestService.advanceStatus(1)).thenReturn(r);

        ResponseEntity<ResourceRequest> res = controller.advance(1);

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertSame(r, res.getBody());
    }
}

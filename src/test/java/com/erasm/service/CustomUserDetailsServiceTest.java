package com.erasm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.erasm.entity.User;
import com.erasm.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private CustomUserDetailsService service;

    @Test
    void loadUserByUsername_found_returnsUser() {
        User user = new User();
        user.setEmail("sagar@erasm.com");
        when(userRepository.findByEmail("sagar@erasm.com")).thenReturn(Optional.of(user));

        UserDetails result = service.loadUserByUsername("sagar@erasm.com");

        assertNotNull(result);
        assertSame(user, result);
    }

    @Test
    void loadUserByUsername_notFound_throws() {
        when(userRepository.findByEmail("missing@erasm.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("missing@erasm.com"));
    }
}

package com.ozkayret.banking.service;

import com.ozkayret.banking.dtos.JwtResponse;
import com.ozkayret.banking.dtos.LoginRequest;
import com.ozkayret.banking.dtos.RegisterRequest;
import com.ozkayret.banking.entity.User;
import com.ozkayret.banking.entity.UserDetailsImpl;
import com.ozkayret.banking.jwt.JwtUtils;
import com.ozkayret.banking.mapper.UserMapper;
import com.ozkayret.banking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void register() {
        RegisterRequest request = mock(RegisterRequest.class);
        when(request.username()).thenReturn("user");
        when(request.email()).thenReturn("user@mail.com");
        when(request.password()).thenReturn("123456");

        when(userRepository.existsByUsername("user1")).thenReturn(false);
        when(userRepository.existsByEmail("user1@mail.com")).thenReturn(false);

        User user = new User();
        when(userMapper.registerDtoToUser(request)).thenReturn(user);
        when(passwordEncoder.encode("123456")).thenReturn("abcd");

        userService.register(request);

        assertEquals("abcd", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void login() {
        LoginRequest request = mock(LoginRequest.class);
        when(request.username()).thenReturn("user1");
        when(request.password()).thenReturn("pass");

        Authentication authentication = mock(Authentication.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateToken(userDetails)).thenReturn("jwt-token");

        JwtResponse response = userService.login(request);

        assertThat("jwt-token").isEqualTo(response.jwt());
        assertThat(authentication).isEqualTo(SecurityContextHolder.getContext().getAuthentication());
    }
}
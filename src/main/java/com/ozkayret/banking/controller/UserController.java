package com.ozkayret.banking.controller;

import com.ozkayret.banking.dtos.JwtResponse;
import com.ozkayret.banking.dtos.LoginRequest;
import com.ozkayret.banking.dtos.RegisterRequest;
import com.ozkayret.banking.dtos.SigningResponse;
import com.ozkayret.banking.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //1. Register a New User
//- POST /api/users/register
//- Registers a new user with username, password, and email.
    @PostMapping("register")
    public void register(@RequestBody @Valid RegisterRequest request) {
        userService.register(request);
    }

    // 2. User Login
//- POST /api/users/login
//- Authenticates a user and returns a JWT for accessing protected endpoints.
    @PostMapping("login")
    public JwtResponse login(@RequestBody @Valid LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("signing")
    public ResponseEntity<SigningResponse> signing(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        var jwt = userService.login(request);
        Cookie cookie = new Cookie("jwt", jwt.jwt());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 1 day

        response.addCookie(cookie);
        return ResponseEntity.ok( new SigningResponse(request.username())) ;
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("jwt", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(1); // 1 milisecond
        response.addCookie(cookie);
       return ResponseEntity.ok().build();
    }
}


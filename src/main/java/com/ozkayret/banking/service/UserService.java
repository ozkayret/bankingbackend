package com.ozkayret.banking.service;

import com.ozkayret.banking.dtos.JwtResponse;
import com.ozkayret.banking.dtos.LoginRequest;
import com.ozkayret.banking.dtos.RegisterRequest;
import com.ozkayret.banking.dtos.SigningResponse;
import jakarta.validation.Valid;

public interface UserService {
    void register(RegisterRequest request);

    JwtResponse login(LoginRequest request);

}

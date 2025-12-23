package com.ozkayret.banking.mapper;

import com.ozkayret.banking.dtos.RegisterRequest;
import com.ozkayret.banking.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User registerDtoToUser(RegisterRequest request) {

        return  User.builder()
                .username(request.username())
                .password(request.password())
                .email(request.email())
                .name(request.name())
                .surname(request.surname())
                .build();
    }
}

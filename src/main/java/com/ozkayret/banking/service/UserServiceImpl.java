package com.ozkayret.banking.service;

import com.ozkayret.banking.dtos.JwtResponse;
import com.ozkayret.banking.dtos.LoginRequest;
import com.ozkayret.banking.dtos.RegisterRequest;
import com.ozkayret.banking.entity.User;
import com.ozkayret.banking.entity.UserDetailsImpl;
import com.ozkayret.banking.exception.EmailExistException;
import com.ozkayret.banking.exception.UserNameExistException;
import com.ozkayret.banking.jwt.JwtUtils;
import com.ozkayret.banking.mapper.UserMapper;
import com.ozkayret.banking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    @Override
    public void register(RegisterRequest request) {

        if (Boolean.TRUE.equals(userRepository.existsByUsername(request.username()))) {
            log.error(messageSource.getMessage("username.exists", null, LocaleContextHolder.getLocale()));
            throw new UserNameExistException(
                    messageSource.getMessage("username.exists", null, LocaleContextHolder.getLocale()));
        }

        if (Boolean.TRUE.equals(userRepository.existsByEmail(request.email()))) {
            log.error(messageSource.getMessage("email.exists", null, LocaleContextHolder.getLocale()));
            throw new EmailExistException(
                    messageSource.getMessage("email.exists", null, LocaleContextHolder.getLocale()));
        }

        User user = userMapper.registerDtoToUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);
        log.debug("--> User created...");
    }

    @Override
    public JwtResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails);
        return new JwtResponse(jwt);
    }

}

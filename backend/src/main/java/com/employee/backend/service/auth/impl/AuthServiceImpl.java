package com.employee.backend.service.auth.impl;

import com.employee.backend.dto.request.*;
import com.employee.backend.dto.response.*;
import com.employee.backend.entity.*;
import com.employee.backend.exception.*;
import com.employee.backend.repository.*;
import com.employee.backend.security.JwtUtils;
import com.employee.backend.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        String token = jwtUtils.generateToken(request.getUsername());
        UserEntity user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        return LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }
}

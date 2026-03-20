package com.employee.backend.service.auth;

import com.employee.backend.dto.request.LoginRequest;
import com.employee.backend.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}

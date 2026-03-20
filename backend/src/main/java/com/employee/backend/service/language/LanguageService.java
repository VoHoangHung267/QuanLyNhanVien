package com.employee.backend.service.language;

import com.employee.backend.dto.response.LanguageResponse;

import java.util.List;

public interface LanguageService {
    List<LanguageResponse> findAll();
}

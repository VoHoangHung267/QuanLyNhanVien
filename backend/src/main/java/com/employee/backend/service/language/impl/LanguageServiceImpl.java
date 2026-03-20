package com.employee.backend.service.language.impl;

import com.employee.backend.dto.request.*;
import com.employee.backend.dto.response.*;
import com.employee.backend.entity.*;
import com.employee.backend.exception.*;
import com.employee.backend.repository.*;
import com.employee.backend.service.language.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;

    @Override
    public List<LanguageResponse> findAll() {
        return languageRepository.findAll().stream()
                .map(l -> LanguageResponse.builder().id(l.getId()).name(l.getName()).level(l.getLevel()).build())
                .collect(Collectors.toList());
    }
}

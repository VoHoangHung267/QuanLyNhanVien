package com.employee.backend.service.certificate.impl;

import com.employee.backend.dto.request.*;
import com.employee.backend.dto.response.*;
import com.employee.backend.entity.*;
import com.employee.backend.exception.*;
import com.employee.backend.repository.*;
import com.employee.backend.service.certificate.*;
import com.employee.backend.service.certificate.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
class CertificateServiceImpl implements CertificateService {
    private final CertificateRepository certificateRepository;

    @Override
    public List<CertificateResponse> findAll() {
        return certificateRepository.findAll().stream()
                .map(c -> CertificateResponse.builder().id(c.getId()).name(c.getName()).build())
                .collect(Collectors.toList());
    }
}

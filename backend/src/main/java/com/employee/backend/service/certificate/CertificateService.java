package com.employee.backend.service.certificate;

import com.employee.backend.dto.response.CertificateResponse;

import java.util.List;

public interface CertificateService {
    List<CertificateResponse> findAll();
}

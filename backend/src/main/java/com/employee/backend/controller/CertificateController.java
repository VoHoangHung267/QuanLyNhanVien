package com.employee.backend.controller;

import com.employee.backend.dto.request.*;
import com.employee.backend.dto.response.*;
import com.employee.backend.service.certificate.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CertificateResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(certificateService.findAll()));
    }
}

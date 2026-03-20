package com.employee.backend.service.employee.impl;

import com.employee.backend.dto.request.*;
import com.employee.backend.dto.response.*;
import com.employee.backend.entity.*;
import com.employee.backend.exception.*;
import com.employee.backend.repository.*;
import com.employee.backend.service.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final LanguageRepository languageRepository;
    private final CertificateRepository certificateRepository;

    @Override
    public List<EmployeeResponse> findAll() {
        // Query 1: load employees + languages
        List<EmployeeEntity> employees = employeeRepository.findAllWithLanguages();

        // Query 2: Hibernate tự merge certificates vào (do đã load vào PersistenceContext)
        employeeRepository.findAllWithCertificates();

        return employees.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse findById(Long id) {
        return toResponse(getEmployeeOrThrow(id));
    }

    @Override
    public EmployeeResponse create(EmployeeRequest request) {
        if (employeeRepository.existsByPhone(request.getPhone())) {
            throw new BadRequestException("Phone number already exists");
        }
        EmployeeEntity employee = EmployeeEntity.builder()
                .name(request.getName())
                .dob(request.getDob())
                .address(request.getAddress())
                .phone(request.getPhone())
                .build();
        assignLanguages(employee, request.getLanguageIds());
        assignCertificates(employee, request.getCertificateIds());
        return toResponse(employeeRepository.save(employee));
    }

    @Override
    public EmployeeResponse update(Long id, EmployeeRequest request) {
        EmployeeEntity employee = getEmployeeOrThrow(id);

        if (employeeRepository.existsByPhoneAndIdNot(request.getPhone(), id)) {
            throw new BadRequestException("Phone number already used by another employee");
        }

        employee.setName(request.getName());
        employee.setDob(request.getDob());
        employee.setAddress(request.getAddress());
        employee.setPhone(request.getPhone());

        // Xóa hết quan hệ cũ
        employee.getEmployeeLanguages().clear();
        employee.getEmployeeCertificates().clear();

        // ← THÊM DÒNG NÀY: flush xuống DB trước khi insert mới
        employeeRepository.saveAndFlush(employee);

        // Sau đó mới assign mới
        assignLanguages(employee, request.getLanguageIds());
        assignCertificates(employee, request.getCertificateIds());

        return toResponse(employeeRepository.save(employee));
    }


    @Override
    public void delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new NotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    // ── helpers ──────────────────────────────────────────────────────────────
    private EmployeeEntity getEmployeeOrThrow(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found with id: " + id));
    }

    private void assignLanguages(EmployeeEntity employee, List<Long> ids) {
        if (ids == null) return;
        ids.forEach(langId -> {
            LanguageEntity lang = languageRepository.findById(langId)
                    .orElseThrow(() -> new NotFoundException("Language not found: " + langId));
            employee.getEmployeeLanguages().add(
                    EmployeeLanguageEntity.builder().employee(employee).language(lang).build());
        });
    }

    private void assignCertificates(EmployeeEntity employee, List<Long> ids) {
        if (ids == null) return;
        ids.forEach(certId -> {
            CertificateEntity cert = certificateRepository.findById(certId)
                    .orElseThrow(() -> new NotFoundException("Certificate not found: " + certId));
            employee.getEmployeeCertificates().add(
                    EmployeeCertificateEntity.builder().employee(employee).certificate(cert).build());
        });
    }

    private EmployeeResponse toResponse(EmployeeEntity e) {
        return EmployeeResponse.builder()
                .id(e.getId()).name(e.getName()).dob(e.getDob())
                .address(e.getAddress()).phone(e.getPhone())
                .languages(e.getEmployeeLanguages().stream()
                        .map(el -> LanguageResponse.builder()
                                .id(el.getLanguage().getId())
                                .name(el.getLanguage().getName())
                                .level(el.getLanguage().getLevel()).build())
                        .collect(Collectors.toList()))
                .certificates(e.getEmployeeCertificates().stream()
                        .map(ec -> CertificateResponse.builder()
                                .id(ec.getCertificate().getId())
                                .name(ec.getCertificate().getName()).build())
                        .collect(Collectors.toList()))
                .build();
    }
}
package com.employee.backend.service.employee;

import com.employee.backend.dto.request.EmployeeRequest;
import com.employee.backend.dto.response.EmployeeResponse;

import java.util.List;

public interface EmployeeService {
    List<EmployeeResponse> findAll();
    EmployeeResponse findById(Long id);
    EmployeeResponse create(EmployeeRequest request);
    EmployeeResponse update(Long id, EmployeeRequest request);
    void delete(Long id);
}
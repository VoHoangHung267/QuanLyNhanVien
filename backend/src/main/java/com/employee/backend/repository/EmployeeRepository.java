package com.employee.backend.repository;

import com.employee.backend.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    @Query("SELECT DISTINCT e FROM EmployeeEntity e LEFT JOIN FETCH e.employeeLanguages el LEFT JOIN FETCH el.language")
    List<EmployeeEntity> findAllWithLanguages();

    @Query("SELECT DISTINCT e FROM EmployeeEntity e LEFT JOIN FETCH e.employeeCertificates ec LEFT JOIN FETCH ec.certificate")
    List<EmployeeEntity> findAllWithCertificates();

    boolean existsByPhone(String phone);
    boolean existsByPhoneAndIdNot(String phone, Long id);
}
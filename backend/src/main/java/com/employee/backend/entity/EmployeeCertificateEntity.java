package com.employee.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "employee_certificate",
        uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id","certificate_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EmployeeCertificateEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "certificate_id", nullable = false)
    private CertificateEntity certificate;
}

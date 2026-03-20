package com.employee.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "employee_language",
        uniqueConstraints = @UniqueConstraint(columnNames = {"employee_id","language_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeLanguageEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "language_id", nullable = false)
    private LanguageEntity language;
}

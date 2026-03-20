package com.employee.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "language",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name","level"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LanguageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 10)
    private String level;
}

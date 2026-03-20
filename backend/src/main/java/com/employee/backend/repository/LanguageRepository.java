package com.employee.backend.repository;

import com.employee.backend.entity.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


@Repository
public interface LanguageRepository extends JpaRepository<LanguageEntity, Long> {}

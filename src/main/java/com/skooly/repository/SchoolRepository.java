package com.skooly.repository;

import com.skooly.model.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {
    Optional<School> findByCode(String code);
    boolean existsByCode(String code);
}

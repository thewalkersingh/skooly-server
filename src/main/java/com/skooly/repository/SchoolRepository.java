package com.skooly.repository;

import com.skooly.entity.School;
import com.skooly.enums.SchoolStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {
	
	// ── Lookup ────────────────────────────────────────────────────────────────
	Optional<School> findBySchoolCode(String schoolCode);
	
	Optional<School> findByEmail(String email);
	
	Optional<School> findByPhone(String phone);
	
	// ── Existence checks ──────────────────────────────────────────────────────
	boolean existsBySchoolCode(String schoolCode);
	
	boolean existsByEmail(String email);
	
	boolean existsByPhone(String phone);
	
	// ── Filtered lists ────────────────────────────────────────────────────────
	Page<School> findByStatus(SchoolStatus status, Pageable pageable);
	
	Page<School> findBySchoolNameContainingIgnoreCase(String name, Pageable pageable);
	
}
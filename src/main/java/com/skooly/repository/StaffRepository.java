package com.skooly.repository;
import com.skooly.model.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
	Optional<Staff> findByUserId(Long userId);
	
	boolean existsByEmail(String email);
	
	@Query("""
			    SELECT s FROM Staff s
			    WHERE (:departmentId IS NULL OR s.department.id = :departmentId)
			    AND (:status IS NULL OR s.status = :status)
			    AND (:gender IS NULL OR s.gender = :gender)
			    AND (:search IS NULL
			         OR LOWER(s.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
			         OR LOWER(s.lastName)  LIKE LOWER(CONCAT('%', :search, '%')))
			""")
	Page<Staff> findWithFilters(
			@Param("departmentId") Long departmentId,
			@Param("status") Staff.StaffStatus status,
			@Param("gender") Staff.Gender gender,
			@Param("search") String search,
			Pageable pageable
	                           );
}
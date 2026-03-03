package com.skooly.repository;
import com.skooly.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
	Optional<Teacher> findByUserId(Long userId);
	
	boolean existsByEmail(String email);
	
	Page<Teacher> findByStatus(Teacher.Status status, Pageable pageable);
	
	Page<Teacher> findByGender(Teacher.Gender gender, Pageable pageable);
	
	Page<Teacher> findBySubjectId(Long subjectId, Pageable pageable);
	
	@Query("""
			    SELECT t FROM Teacher t
			    WHERE (:subjectId IS NULL OR t.subject.id = :subjectId)
			    AND (:status IS NULL OR t.status = :status)
			    AND (:gender IS NULL OR t.gender = :gender)
			    AND (:search IS NULL
			         OR LOWER(t.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
			         OR LOWER(t.lastName)  LIKE LOWER(CONCAT('%', :search, '%')))
			""")
	Page<Teacher> findWithFilters(
			@Param("subjectId") Long subjectId,
			@Param("status") Teacher.Status status,
			@Param("gender") Teacher.Gender gender,
			@Param("search") String search,
			Pageable pageable
	                             );
}
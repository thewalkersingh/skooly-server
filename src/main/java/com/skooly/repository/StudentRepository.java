package com.skooly.repository;
import com.skooly.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	Page<Student> findBySchoolClassId(Long classId, Pageable pageable);
	
	Page<Student> findBySectionId(Long sectionId, Pageable pageable);
	
	Page<Student> findByStatus(Student.Status status, Pageable pageable);
	
	Page<Student> findByGender(Student.Gender gender, Pageable pageable);
	
	Page<Student> findByParentId(Long parentId, Pageable pageable);
	
	Optional<Student> findByUserId(Long userId);
	
	boolean existsByEmail(String email);
	
	@Query("""
			   SELECT s FROM Student s WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', :name, '%'))
			    OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :name, '%'))
			""")
	Page<Student> searchByName(@Param("name") String name, Pageable pageable);
	
	@Query("""
			    SELECT s FROM Student s
			    WHERE (:classId IS NULL OR s.schoolClass.id = :classId)
			    AND (:sectionId IS NULL OR s.section.id = :sectionId)
			    AND (:status IS NULL OR s.status = :status)
			    AND (:gender IS NULL OR s.gender = :gender)
			    AND (:search IS NULL OR LOWER(s.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
			         OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :search, '%')))
			""")
	Page<Student> findWithFilters(
			@Param("classId") Long classId,
			@Param("sectionId") Long sectionId,
			@Param("status") Student.Status status,
			@Param("gender") Student.Gender gender,
			@Param("search") String search,
			Pageable pageable
	                             );
}
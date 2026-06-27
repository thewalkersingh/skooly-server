package com.skooly.repository;

import com.skooly.entity.Student;
import com.skooly.enums.StudentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
	
	// ── By Section ────────────────────────────────────────────────────────────
	Page<Student> findBySectionId(Long sectionId, Pageable pageable);
	
	List<Student> findBySectionId(Long sectionId);
	
	long countBySectionId(Long sectionId);
	
	// ── By Classroom (traverse section → classroom) ───────────────────────────
	@Query("SELECT st FROM Student st WHERE st.section.classroom.id = :classroomId")
	Page<Student> findByClassroomId(@Param("classroomId") Long classroomId, Pageable pageable);
	
	// ── By School (traverse section → classroom → school) ────────────────────
	@Query("SELECT st FROM Student st WHERE st.section.classroom.school.id = :schoolId")
	Page<Student> findBySchoolId(@Param("schoolId") Long schoolId, Pageable pageable);
	
	@Query("SELECT COUNT(st) FROM Student st WHERE st.section.classroom.school.id = :schoolId")
	long countBySchoolId(@Param("schoolId") Long schoolId);
	
	// ── By Parent ─────────────────────────────────────────────────────────────
	List<Student> findByParentId(Long parentId);
	
	// ── By Status ─────────────────────────────────────────────────────────────
	Page<Student> findByStudentStatus(StudentStatus status, Pageable pageable);
	
	Page<Student> findBySectionIdAndStudentStatus(Long sectionId, StudentStatus status, Pageable pageable);
	
	// ── Lookup via UserIdentity ───────────────────────────────────────────────
	Optional<Student> findByIdentityId(Long identityId);
	
	Optional<Student> findByIdentityPhone(String phone);
	
	Optional<Student> findByIdentityEmail(String email);
	
	// ── Existence ─────────────────────────────────────────────────────────────
	boolean existsByIdentityPhone(String phone);
	
	boolean existsByIdentityEmail(String email);
	
	// ── Search by name within a school ────────────────────────────────────────
	@Query("""
		 SELECT st FROM Student st
		 WHERE st.section.classroom.school.id = :schoolId
		 AND (LOWER(st.identity.firstName) LIKE LOWER(CONCAT('%', :name, '%'))
		   OR LOWER(st.identity.lastName)  LIKE LOWER(CONCAT('%', :name, '%')))
		 """)
	Page<Student> searchByNameAndSchoolId(@Param("schoolId") Long schoolId,
		 @Param("name") String name,
		 Pageable pageable);
	
	// ── With identity + section + classroom eagerly fetched ───────────────────
	// Use for student detail page — prevents N+1 on nested lazy loads
	@Query("""
		 SELECT st FROM Student st
		 JOIN FETCH st.identity
		 JOIN FETCH st.section sec
		 JOIN FETCH sec.classroom
		 WHERE st.id = :id
		 """)
	Optional<Student> findByIdWithDetails(@Param("id") Long id);
	
	// ── Students with no parent linked yet ────────────────────────────────────
	@Query("SELECT st FROM Student st WHERE st.parent IS NULL AND st.section.classroom.school.id = :schoolId")
	List<Student> findStudentsWithoutParentBySchoolId(@Param("schoolId") Long schoolId);
	
}
package com.skooly.repository;

import com.skooly.entity.Teacher;
import com.skooly.enums.TeacherStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
	
	// ── By School ─────────────────────────────────────────────────────────────
	Page<Teacher> findBySchoolId(Long schoolId, Pageable pageable);
	
	List<Teacher> findBySchoolId(Long schoolId);
	
	Page<Teacher> findBySchoolIdAndTeacherStatus(Long schoolId, TeacherStatus teacherStatus, Pageable pageable);
	
	// ── By Status ─────────────────────────────────────────────────────────────
	Page<Teacher> findByTeacherStatus(TeacherStatus teacherStatus, Pageable pageable);
	
	// ── Lookup via UserIdentity ───────────────────────────────────────────────
	Optional<Teacher> findByIdentityId(Long identityId);
	
	Optional<Teacher> findByIdentityEmail(String email);
	
	Optional<Teacher> findByIdentityPhone(String phone);
	
	// ── Existence ─────────────────────────────────────────────────────────────
	boolean existsByIdentityEmail(String email);
	
	boolean existsByIdentityPhone(String phone);
	
	// ── Search by name (first or last) ───────────────────────────────────────
	@Query("""
		SELECT t FROM Teacher t
		WHERE t.school.id = :schoolId
		AND (LOWER(t.identity.firstName) LIKE LOWER(CONCAT('%', :name, '%'))
		  OR LOWER(t.identity.lastName)  LIKE LOWER(CONCAT('%', :name, '%')))
		""")
	Page<Teacher> searchByNameAndSchoolId(@Param("schoolId") Long schoolId, @Param("name") String name,
		Pageable pageable);
	
	// ── Teachers assigned to a subject ───────────────────────────────────────
//	@Query("SELECT t FROM Teacher t JOIN t.subjects s WHERE s.id = :subjectId")
	@Query("SELECT t FROM Subject s JOIN s.teachers t WHERE s.id = :subjectId")
	List<Teacher> findTeachersBySubjectId(@Param("subjectId") Long subjectId);
	
	// ── Teachers assigned to a section (class teacher) ────────────────────────
	@Query("SELECT t FROM Teacher t WHERE t.id IN " +
		       "(SELECT sec.teacher.id FROM Section sec WHERE sec.id = :sectionId)")
	Optional<Teacher> findClassTeacherBySectionId(@Param("sectionId") Long sectionId);
	
	// ── Teachers with no section assigned (available pool) ───────────────────
	@Query("""
		SELECT t FROM Teacher t
		WHERE t.school.id = :schoolId
		AND t.teacherStatus = 'ACTIVE'
		AND t.id NOT IN (
		    SELECT sec.teacher.id FROM Section sec
		    WHERE sec.teacher IS NOT NULL
		    AND sec.classroom.school.id = :schoolId
		)
		""")
	List<Teacher> findUnassignedTeachersBySchoolId(@Param("schoolId") Long schoolId);
	
	// ── With identity eagerly fetched (avoids N+1 in lists) ──────────────────
	@Query("SELECT t FROM Teacher t JOIN FETCH t.identity WHERE t.school.id = :schoolId")
	List<Teacher> findBySchoolIdWithIdentity(@Param("schoolId") Long schoolId);
	
}
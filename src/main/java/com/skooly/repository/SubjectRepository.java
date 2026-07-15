package com.skooly.repository;

import com.skooly.entity.Subject;
import com.skooly.enums.SubjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
	
	// ── Lookup ────────────────────────────────────────────────────────────────
	Optional<Subject> findBySubjectCode(String subjectCode);
	
	// ── Existence ─────────────────────────────────────────────────────────────
	boolean existsBySubjectCode(String subjectCode);
	
	// ── By Status ─────────────────────────────────────────────────────────────
	Page<Subject> findBySubjectStatus(SubjectStatus subjectStatus, Pageable pageable);
	
	List<Subject> findBySubjectStatus(SubjectStatus subjectStatus);
	
	// ── Subjects assigned to a section (via section_subjects join table) ──────
	@Query("SELECT subj FROM Section sec JOIN sec.subjects subj WHERE sec.id = :sectionId")
	List<Subject> findBySectionId(@Param("sectionId") Long sectionId);
	
	// ── Subjects taught by a teacher (via subject_teachers join table) ─────────
	@Query("SELECT s FROM Subject s JOIN s.teachers t WHERE t.id = :teacherId")
	List<Subject> findByTeacherId(@Param("teacherId") Long teacherId);
	
	// ── Subjects NOT yet assigned to a section (for assignment dropdown) ──────
	@Query("""
		     SELECT s FROM Subject s
		     WHERE s.id NOT IN (SELECT subj.id FROM Section sec JOIN sec.subjects subj WHERE sec.id = :sectionId)
				AND s.subjectStatus = 'ACTIVE'
		""")
	List<Subject> findSubjectsNotInSection(@Param("sectionId") Long sectionId);
	
	// ── Subjects NOT yet assigned to a teacher ────────────────────────────────
	@Query("""
		 SELECT s FROM Subject s
		 WHERE s.id NOT IN (SELECT sub.id FROM Subject sub JOIN sub.teachers t WHERE t.id = :teacherId)
		AND s.subjectStatus = 'ACTIVE'
		""")
	List<Subject> findSubjectsNotAssignedToTeacher(@Param("teacherId") Long teacherId);
	
	// ── Search by name ────────────────────────────────────────────────────────
	Page<Subject> findBySubjectNameContainingIgnoreCase(String name, Pageable pageable);
	
	// ── With teachers eagerly fetched (avoids N+1) ────────────────────────────
	@Query("SELECT DISTINCT s FROM Subject s LEFT JOIN FETCH s.teachers WHERE s.id = :id")
	Optional<Subject> findSubjectsByIdWithTeachers(@Param("id") Long id);
	
}
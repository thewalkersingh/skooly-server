package com.skooly.repository;

import com.skooly.entity.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {
	
	// ── By Classroom ──────────────────────────────────────────────────────────
	List<Section> findByClassroomId(Long classroomId);
	
	Page<Section> findByClassroomId(Long classroomId, Pageable pageable);
	
	// ── By School (traverse classroom → school) ───────────────────────────────
	@Query("SELECT s FROM Section s WHERE s.classroom.school.id = :schoolId")
	Page<Section> findBySchoolId(@Param("schoolId") Long schoolId, Pageable pageable);
	
	@Query("SELECT s FROM Section s WHERE s.classroom.school.id = :schoolId")
	List<Section> findBySchoolId(@Param("schoolId") Long schoolId);
	
	// ── By Teacher ────────────────────────────────────────────────────────────
	List<Section> findByTeacherId(Long teacherId);
	
	Page<Section> findByTeacherId(Long teacherId, Pageable pageable);
	
	// ── Sections with no teacher assigned yet ─────────────────────────────────
	@Query("SELECT s FROM Section s WHERE s.teacher IS NULL AND s.classroom.school.id = :schoolId")
	List<Section> findUnassignedSectionsBySchoolId(@Param("schoolId") Long schoolId);
	
	// ── Lookup ────────────────────────────────────────────────────────────────
	Optional<Section> findByClassroomIdAndSectionName(Long classroomId, String sectionName);
	
	// ── Existence ─────────────────────────────────────────────────────────────
	boolean existsByClassroomIdAndSectionName(Long classroomId, String sectionName);
	
	// ── With subjects eagerly fetched (avoids N+1 when rendering section detail) ──
	@Query("SELECT DISTINCT s FROM Section s LEFT JOIN FETCH s.subjects WHERE s.id = :id")
	Optional<Section> findByIdWithSubjects(@Param("id") Long id);
	
	// ── All sections of a classroom with subjects ──────────────────────────────
	@Query("SELECT DISTINCT s FROM Section s LEFT JOIN FETCH s.subjects WHERE s.classroom.id = :classroomId")
	List<Section> findByClassroomIdWithSubjects(@Param("classroomId") Long classroomId);
	
	// ── Student count per section (avoids loading student list) ───────────────
	@Query("""
		SELECT s, COUNT(st.id) FROM Section s
		LEFT JOIN Student st ON st.section.id = s.id
		WHERE s.classroom.id = :classroomId
		GROUP BY s.id
		""")
	List<Object[]> findSectionsWithStudentCountByClassroomId(@Param("classroomId") Long classroomId);
	
	@Query("SELECT DISTINCT s FROM Section s LEFT JOIN FETCH s.subjects LEFT JOIN FETCH s.classroom")
	List<Section> findAllWithSubjectsAndClassroom();
	
}
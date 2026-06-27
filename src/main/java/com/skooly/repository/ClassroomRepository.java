package com.skooly.repository;

import com.skooly.entity.Classroom;
import com.skooly.enums.ClassroomStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
	
	// ── By School ─────────────────────────────────────────────────────────────
	Page<Classroom> findBySchoolId(Long schoolId, Pageable pageable);
	
	List<Classroom> findBySchoolId(Long schoolId);
	
	Page<Classroom> findBySchoolIdAndStatus(Long schoolId, ClassroomStatus status, Pageable pageable);
	
	// ── Lookup ────────────────────────────────────────────────────────────────
	Optional<Classroom> findByClassroomCode(String classroomCode);
	
	Optional<Classroom> findBySchoolIdAndClassroomCode(Long schoolId, String classroomCode);
	
	// ── Existence checks ──────────────────────────────────────────────────────
	boolean existsByClassroomCode(String classroomCode);
	
	boolean existsBySchoolIdAndClassroomCode(Long schoolId, String classroomCode);
	
	// ── Status filter ─────────────────────────────────────────────────────────
	Page<Classroom> findByStatus(ClassroomStatus status, Pageable pageable);
	
	// ── With section count (avoids loading section list) ─────────────────────
	@Query("""
		 SELECT c, COUNT(s.id) FROM Classroom c
		 LEFT JOIN Section s ON s.classroom.id = c.id
		 WHERE c.school.id = :schoolId GROUP BY c.id
		 """)
	List<Object[]> findClassroomsWithSectionCountBySchoolId(@Param("schoolId") Long schoolId);
	
}
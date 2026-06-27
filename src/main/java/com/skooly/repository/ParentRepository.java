package com.skooly.repository;

import com.skooly.entity.Parent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent, Long> {
	
	// ── Lookup via UserIdentity ───────────────────────────────────────────────
	Optional<Parent> findByIdentityId(Long identityId);
	
	Optional<Parent> findByIdentityPhone(String phone);
	
	Optional<Parent> findByIdentityEmail(String email);
	
	// ── Existence ─────────────────────────────────────────────────────────────
	boolean existsByIdentityPhone(String phone);
	
	boolean existsByIdentityEmail(String email);
	
	// ── Parents belonging to a school (via their students) ────────────────────
	@Query("""
		 SELECT DISTINCT p FROM Parent p
		 JOIN Student st ON st.parent.id = p.id
		 WHERE st.section.classroom.school.id = :schoolId
		 """)
	Page<Parent> findBySchoolId(@Param("schoolId") Long schoolId, Pageable pageable);
	
	// ── Search by name ────────────────────────────────────────────────────────
	@Query("""
		 SELECT p FROM Parent p
		 WHERE LOWER(p.identity.firstName) LIKE LOWER(CONCAT('%', :name, '%'))
		    OR LOWER(p.identity.lastName)  LIKE LOWER(CONCAT('%', :name, '%'))
		 """)
	Page<Parent> searchByName(@Param("name") String name, Pageable pageable);
	
	// ── Parents with multiple children enrolled ────────────────────────────────
	@Query("""
		 SELECT p FROM Parent p
		 WHERE (SELECT COUNT(st) FROM Student st WHERE st.parent.id = p.id) > 1
		 AND (SELECT COUNT(st) FROM Student st
		      WHERE st.parent.id = p.id
		      AND st.section.classroom.school.id = :schoolId) > 0
		 """)
	List<Parent> findParentsWithMultipleChildrenBySchoolId(@Param("schoolId") Long schoolId);
	
	// ── With identity eagerly fetched ─────────────────────────────────────────
	@Query("SELECT p FROM Parent p JOIN FETCH p.identity WHERE p.id = :id")
	Optional<Parent> findByIdWithIdentity(@Param("id") Long id);
}
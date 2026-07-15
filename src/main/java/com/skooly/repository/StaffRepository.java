package com.skooly.repository;

import com.skooly.entity.Staff;
import com.skooly.enums.Department;
import com.skooly.enums.StaffRole;
import com.skooly.enums.StaffStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
	
	// ── By School ─────────────────────────────────────────────────────────────
	Page<Staff> findBySchoolId(Long schoolId, Pageable pageable);
	
	List<Staff> findBySchoolId(Long schoolId);
	
	Page<Staff> findBySchoolIdAndStaffStatus(Long schoolId, StaffStatus staffStatus, Pageable pageable);
	
	// ── By Role ───────────────────────────────────────────────────────────────
	List<Staff> findByStaffRole(StaffRole staffRole);
	
	Page<Staff> findByStaffRole(StaffRole staffRole, Pageable pageable);
	
	List<Staff> findBySchoolIdAndStaffRole(Long schoolId, StaffRole staffRole);
	
	// ── By Department ─────────────────────────────────────────────────────────
	List<Staff> findByDepartment(Department department);
	
	List<Staff> findBySchoolIdAndDepartment(Long schoolId, Department department);
	
	// ── By Status ─────────────────────────────────────────────────────────────
	Page<Staff> findByStaffStatus(StaffStatus staffStatus, Pageable pageable);
	
	// ── Lookup via UserIdentity ───────────────────────────────────────────────
	Optional<Staff> findByIdentityId(Long identityId);
	
	Optional<Staff> findByIdentityPhone(String phone);
	
	Optional<Staff> findByIdentityEmail(String email);
	
	// ── Existence ─────────────────────────────────────────────────────────────
	boolean existsByIdentityPhone(String phone);
	
	boolean existsByIdentityEmail(String email);
	
	// ── Search by name ────────────────────────────────────────────────────────
	@Query("""
		SELECT s FROM Staff s
		WHERE s.school.id = :schoolId
		AND (LOWER(s.identity.firstName) LIKE LOWER(CONCAT('%', :name, '%'))
		  OR LOWER(s.identity.lastName)  LIKE LOWER(CONCAT('%', :name, '%')))
		""")
	Page<Staff> searchByNameAndSchoolId(@Param("schoolId") Long schoolId, @Param("name") String name,
		Pageable pageable);
	
	// ── With identity eagerly fetched (avoids N+1 in lists) ──────────────────
	@Query("SELECT s FROM Staff s JOIN FETCH s.identity WHERE s.school.id = :schoolId")
	List<Staff> findBySchoolIdWithIdentity(@Param("schoolId") Long schoolId);
	
	// ── Count by role per school (for dashboard) ──────────────────────────────
	@Query("SELECT COUNT(s) FROM Staff s WHERE s.school.id = :schoolId AND s.staffRole = :staffRole")
	long countBySchoolIdAndStaffRole(@Param("schoolId") Long schoolId, @Param("staffRole") StaffRole staffRole);
	
}
package com.skooly.repository;

import com.skooly.entity.User;
import com.skooly.enums.UserRole;
import com.skooly.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	// ── Core lookup — used by UserDetailsServiceImpl ──────────────────────────
	// Finds by email OR phone — supports both login identifiers
	@Query("SELECT u FROM User u WHERE u.identity.email = :email OR u.identity.phone = :phone")
	Optional<User> findByIdentityEmailOrIdentityPhone(
		@Param("email") String email,
		@Param("phone") String phone);
	
	// ── Individual lookups ────────────────────────────────────────────────────
	Optional<User> findByIdentityEmail(String email);
	
	Optional<User> findByIdentityPhone(String phone);
	
	Optional<User> findByRoleEntityIdAndRole(Long roleEntityId, UserRole role);
	
	// ── Existence checks ──────────────────────────────────────────────────────
	boolean existsByIdentityEmail(String email);
	
	boolean existsByIdentityPhone(String phone);
	
	// ── By Status — for admin management ─────────────────────────────────────
	Page<User> findByStatus(UserStatus status, Pageable pageable);
	
	List<User> findByStatus(UserStatus status);
	
	// ── Pending approvals — ADMIN dashboard ───────────────────────────────────
	@Query("SELECT u FROM User u WHERE u.status = 'PENDING' ORDER BY u.createdAt ASC")
	List<User> findAllPendingApprovals();
	
	// ── By Role ───────────────────────────────────────────────────────────────
	Page<User> findByRole(UserRole role, Pageable pageable);
	
}
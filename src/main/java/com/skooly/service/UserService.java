package com.skooly.service;

import com.skooly.dto.response.auth.UserResponse;
import com.skooly.enums.UserRole;
import com.skooly.enums.UserStatus;
import com.skooly.wrapper.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	UserResponse getUser(Long userId);
	
	UserResponse getUserByPhone(String phone);
	
	UserResponse getUserByEmail(String email);
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	PageResponse<UserResponse> getAllUsers(Pageable pageable);
	
	PageResponse<UserResponse> getUsersByRole(UserRole userRole, Pageable pageable);
	
	PageResponse<UserResponse> getUsersByStatus(UserStatus userStatus, Pageable pageable);
	
	// ── Pending approvals — admin dashboard ───────────────────────────────────
	List<UserResponse> getPendingApprovals();
	
	// ── Status management ─────────────────────────────────────────────────────
	UserResponse updateStatus(Long userId, UserStatus userStatus);
	
	// ── Delete ────────────────────────────────────────────────────────────────
	void deleteUser(Long userId);   // soft delete → DELETED status
	
}
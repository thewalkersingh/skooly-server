package com.skooly.controller;

import com.skooly.dto.response.auth.UserResponse;
import com.skooly.enums.UserRole;
import com.skooly.enums.UserStatus;
import com.skooly.service.UserService;
import com.skooly.wrapper.ApiResponse;
import com.skooly.wrapper.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for managing users")
public class UserController {
	
	private final UserService userService;
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	// GET /users/{userId}
	@GetMapping("/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<UserResponse> getUser(@PathVariable Long userId) {
		return ApiResponse.<UserResponse>builder()
		                  .success(true)
		                  .message("User fetched successfully")
		                  .data(userService.getUser(userId))
		                  .statusCode(200)
		                  .build();
	}
	
	// GET /users/phone/{phone}
	@GetMapping("/phone/{phone}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<UserResponse> getUserByPhone(@PathVariable String phone) {
		return ApiResponse.<UserResponse>builder()
		                  .success(true)
		                  .message("User fetched successfully")
		                  .data(userService.getUserByPhone(phone))
		                  .statusCode(200)
		                  .build();
	}
	
	// GET /users/email/{email}
	@GetMapping("/email/{email}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<UserResponse> getUserByEmail(@PathVariable String email) {
		return ApiResponse.<UserResponse>builder()
		                  .success(true)
		                  .message("User fetched successfully")
		                  .data(userService.getUserByEmail(email))
		                  .statusCode(200)
		                  .build();
	}
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	// GET /users?page=0&size=10
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<PageResponse<UserResponse>> getAllUsers(Pageable pageable) {
		return ApiResponse.<PageResponse<UserResponse>>builder()
		                  .success(true)
		                  .message("Users fetched successfully")
		                  .data(userService.getAllUsers(pageable))
		                  .statusCode(200)
		                  .build();
	}
	
	// GET /users/role/{role}?page=0&size=10
	@GetMapping("/role/{role}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<PageResponse<UserResponse>> getUsersByRole(
		@PathVariable UserRole role,
		Pageable pageable) {
		return ApiResponse.<PageResponse<UserResponse>>builder()
		                  .success(true)
		                  .message("Users fetched successfully")
		                  .data(userService.getUsersByRole(role, pageable))
		                  .statusCode(200)
		                  .build();
	}
	
	// GET /users/status/{status}?page=0&size=10
	@GetMapping("/status/{status}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<PageResponse<UserResponse>> getUsersByStatus(
		@PathVariable UserStatus status,
		Pageable pageable) {
		return ApiResponse.<PageResponse<UserResponse>>builder()
		                  .success(true)
		                  .message("Users fetched successfully")
		                  .data(userService.getUsersByStatus(status, pageable))
		                  .statusCode(200)
		                  .build();
	}
	
	// ── Pending approvals ─────────────────────────────────────────────────────
	// GET /users/pending
	@GetMapping("/pending")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<List<UserResponse>> getPendingApprovals() {
		return ApiResponse.<List<UserResponse>>builder()
		                  .success(true)
		                  .message("Pending approvals fetched successfully")
		                  .data(userService.getPendingApprovals())
		                  .statusCode(200)
		                  .build();
	}
	
	// ── Status management ─────────────────────────────────────────────────────
	// PATCH /users/{userId}/status/{status}
	@PatchMapping("/{userId}/status/{status}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<UserResponse> updateStatus(
		@PathVariable Long userId,
		@PathVariable UserStatus status) {
		return ApiResponse.<UserResponse>builder()
		                  .success(true)
		                  .message("User status updated successfully")
		                  .data(userService.updateStatus(userId, status))
		                  .statusCode(200)
		                  .build();
	}
	
	// ── Delete ────────────────────────────────────────────────────────────────
	// DELETE /users/{userId}
	@DeleteMapping("/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<Void> deleteUser(@PathVariable Long userId) {
		userService.deleteUser(userId);
		return ApiResponse.<Void>builder()
		                  .success(true)
		                  .message("User deleted successfully")
		                  .statusCode(200)
		                  .build();
	}
	
}
package com.skooly.controller;
import com.skooly.dto.common.ApiResponse;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.*;
import com.skooly.dto.response.AuthResponse;
import com.skooly.dto.response.UserResponse;
import com.skooly.model.User;
import com.skooly.repository.UserRepository;
import com.skooly.security.UserPrincipal;
import com.skooly.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final UserRepository userRepository;
	
	// ── Auth Endpoints ───────────────────────────────────────────────────────
	
	@PostMapping("/auth/login")
	public ResponseEntity<ApiResponse<AuthResponse>> login(
			@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Login successful",
		                                           authService.login(request)));
	}
	
	@PostMapping("/auth/logout")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponse<Void>> logout(
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		authService.logout(userPrincipal.getId());
		return ResponseEntity.ok(new ApiResponse<>(true, "Logout successful", null));
	}
	
	@PostMapping("/auth/refresh-token")
	public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(
			@Valid @RequestBody RefreshTokenRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Token refreshed successfully",
		                                           authService.refreshToken(request)));
	}
	
	@PostMapping("/auth/forgot-password")
	public ResponseEntity<ApiResponse<Void>> forgotPassword(
			@Valid @RequestBody ForgotPasswordRequest request) {
		authService.forgotPassword(request);
		return ResponseEntity.ok(new ApiResponse<>(true,
		                                           "If the username exists, a reset link has been generated", null));
	}
	
	@PostMapping("/auth/reset-password")
	public ResponseEntity<ApiResponse<Void>> resetPassword(
			@Valid @RequestBody ResetPasswordRequest request) {
		authService.resetPassword(request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Password reset successful", null));
	}
	
	@PostMapping("/auth/change-password")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponse<Void>> changePassword(
			@AuthenticationPrincipal UserPrincipal userPrincipal,
			@Valid @RequestBody ChangePasswordRequest request) {
		authService.changePassword(userPrincipal.getId(), request);
		return ResponseEntity.ok(new ApiResponse<>(true, "Password changed successfully", null));
	}
	
	@GetMapping("/users/me")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser(
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		return ResponseEntity.ok(new ApiResponse<>(true, "User fetched successfully",
		                                           authService.getCurrentUser(userPrincipal.getId())));
	}
	
	// ── User Management ──────────────────────────────────────────────────────
	
	@GetMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getAllUsers(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		
		Page<User> users = userRepository.findAll(PageRequest.of(page-1, size));
		List<UserResponse> data = users.getContent().stream()
				                          .map(u -> UserResponse.builder()
						                                    .id(u.getId())
						                                    .username(u.getUsername())
						                                    .role(u.getRole().getName())
						                                    .isActive(u.getIsActive())
						                                    .createdAt(u.getCreatedAt())
						                                    .build())
				                          .toList();
		
		return ResponseEntity.ok(new ApiResponse<>(true, "Users fetched successfully",
		                                           new PageResponse<>(data, page, size, users.getTotalElements(),
		                                                              users.getTotalPages())));
	}
	
	@GetMapping("/users/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "User fetched successfully",
		                                           authService.getCurrentUser(id)));
	}
	
	@PostMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<UserResponse>> createUser(
			@Valid @RequestBody CreateUserRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "User created successfully",
		                                                         authService.createUser(request)));
	}
	
	@PutMapping("/users/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<UserResponse>> updateUser(
			@PathVariable Long id,
			@Valid @RequestBody CreateUserRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "User updated successfully",
		                                           authService.updateUser(id, request)));
	}
	
	@DeleteMapping("/users/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
		authService.deleteUser(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully", null));
	}
	
	@PatchMapping("/users/{id}/status")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> updateUserStatus(
			@PathVariable Long id,
			@RequestParam Boolean isActive) {
		authService.updateUserStatus(id, isActive);
		return ResponseEntity.ok(new ApiResponse<>(true, "User status updated", null));
	}
}
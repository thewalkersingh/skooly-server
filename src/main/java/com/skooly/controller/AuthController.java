package com.skooly.controller;

import com.skooly.dto.request.auth.*;
import com.skooly.dto.response.auth.AuthMessageResponse;
import com.skooly.dto.response.auth.LoginResponse;
import com.skooly.dto.response.auth.MeResponse;
import com.skooly.dto.response.auth.UserResponse;
import com.skooly.security.CustomUserDetails;
import com.skooly.service.AuthService;
import com.skooly.service.UserService;
import com.skooly.wrapper.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	private final UserService userService;
	
	// ── Self Registration ─────────────────────────────────────────────────────
	// Public — no token needed
	// Creates PENDING user — admin must approve before login is possible
	// POST /auth/register
	@PostMapping("/register")
	public ApiResponse<AuthMessageResponse> register(@Valid @RequestBody RegisterRequest request) {
		
		AuthMessageResponse response = authService.register(request);
		return ApiResponse.<AuthMessageResponse>builder()
		                  .success(true)
		                  .message("Registration submitted successfully")
		                  .data(response)
		                  .statusCode(200)
		                  .build();
	}
	
	// ── Admin Creates Account ─────────────────────────────────────────────────
	// ADMIN only — handled by SecurityConfig
	// POST /auth/create-account
	@PostMapping("/create-account")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<AuthMessageResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
		
		AuthMessageResponse response = authService.createAccount(request);
		return ApiResponse.<AuthMessageResponse>builder()
		                  .success(true)
		                  .message("Account created successfully")
		                  .data(response)
		                  .statusCode(200)
		                  .build();
	}
	
	// ── Admin Approve / Reject ────────────────────────────────────────────────
	// POST /auth/approve/{userId}
	@PostMapping("/approve/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<AuthMessageResponse> approveAccount(@PathVariable Long userId) {
		
		AuthMessageResponse response = authService.approveAccount(userId);
		return ApiResponse.<AuthMessageResponse>builder()
		                  .success(true)
		                  .message("Account approved successfully")
		                  .data(response)
		                  .statusCode(200)
		                  .build();
	}
	
	// POST /auth/reject/{userId}
	@PostMapping("/reject/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<AuthMessageResponse> rejectAccount(@PathVariable Long userId,
		@RequestParam(required = false) String reason) {
		
		AuthMessageResponse response = authService.rejectAccount(userId, reason);
		return ApiResponse.<AuthMessageResponse>builder()
		                  .success(true)
		                  .message("Account rejected")
		                  .data(response)
		                  .statusCode(200)
		                  .build();
	}
	
	// ── Pending Approvals list ────────────────────────────────────────────────
	// GET /auth/pending
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
	
	// ── Login ─────────────────────────────────────────────────────────────────
	// Public — no token needed
	// POST /auth/login
	@PostMapping("/login")
	public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
		
		LoginResponse response = authService.login(request);
		return ApiResponse.<LoginResponse>builder()
		                  .success(true)
		                  .message(response.getMessage() != null
			                           ? response.getMessage()
			                           : "Login successful")
		                  .data(response)
		                  .statusCode(200)
		                  .build();
	}
	
	// ── OTP ───────────────────────────────────────────────────────────────────
	// Public — no token needed
	// POST /auth/verify-otp
	@PostMapping("/verify-otp")
	public ApiResponse<AuthMessageResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
		
		AuthMessageResponse response = authService.verifyOtp(request);
		return ApiResponse.<AuthMessageResponse>builder()
		                  .success(true)
		                  .message("OTP verified successfully")
		                  .data(response)
		                  .statusCode(200)
		                  .build();
	}
	
	// POST /auth/resend-otp
	@PostMapping("/resend-otp")
	public ApiResponse<AuthMessageResponse> resendOtp(@Valid @RequestBody ResendOtpRequest request) {
		
		AuthMessageResponse response = authService.resendOtp(request);
		return ApiResponse.<AuthMessageResponse>builder()
		                  .success(true)
		                  .message("OTP resent successfully")
		                  .data(response)
		                  .statusCode(200)
		                  .build();
	}
	
	// ── First Login — Set Password ────────────────────────────────────────────
	// Public — user doesn't have a token yet at this point
	// POST /auth/set-password
	@PostMapping("/set-password")
	public ApiResponse<LoginResponse> setPassword(@Valid @RequestBody SetPasswordRequest request) {
		
		LoginResponse response = authService.setPassword(request);
		return ApiResponse.<LoginResponse>builder()
		                  .success(true)
		                  .message("Password set successfully. You are now logged in.")
		                  .data(response)
		                  .statusCode(200)
		                  .build();
	}
	
	// ── Password Reset ────────────────────────────────────────────────────────
	// Public — no token needed (user is locked out)
	// POST /auth/forgot-password
	@PostMapping("/forgot-password")
	public ApiResponse<AuthMessageResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
		
		AuthMessageResponse response = authService.forgotPassword(request);
		return ApiResponse.<AuthMessageResponse>builder()
		                  .success(true)
		                  .message("OTP sent to your registered email and phone")
		                  .data(response)
		                  .statusCode(200)
		                  .build();
	}
	
	// POST /auth/reset-password
	@PostMapping("/reset-password")
	public ApiResponse<AuthMessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
		
		AuthMessageResponse response = authService.resetPassword(request);
		return ApiResponse.<AuthMessageResponse>builder()
		                  .success(true)
		                  .message("Password reset successfully")
		                  .data(response)
		                  .statusCode(200)
		                  .build();
	}
	
	// ── Token Management ──────────────────────────────────────────────────────
	// POST /auth/refresh
	// Public — refresh token sent in body (no access token available at this point)
	@PostMapping("/refresh")
	public ApiResponse<LoginResponse> refreshToken(@RequestParam String refreshToken) {
		
		LoginResponse response = authService.refreshToken(refreshToken);
		return ApiResponse.<LoginResponse>builder()
		                  .success(true)
		                  .message("Token refreshed successfully")
		                  .data(response)
		                  .statusCode(200)
		                  .build();
	}
	
	// POST /auth/logout
	// Authenticated — need to know which refresh token to revoke
	@PostMapping("/logout")
	public ApiResponse<AuthMessageResponse> logout(@RequestParam String refreshToken) {
		
		AuthMessageResponse response = authService.logout(refreshToken);
		return ApiResponse.<AuthMessageResponse>builder()
		                  .success(true)
		                  .message("Logged out successfully")
		                  .data(response)
		                  .statusCode(200)
		                  .build();
	}
	
	// ── Current User ──────────────────────────────────────────────────────────
	// GET /auth/me
	// Authenticated — extracts userId from SecurityContext via @AuthenticationPrincipal
	@GetMapping("/me")
	public ApiResponse<MeResponse> getMe(@AuthenticationPrincipal CustomUserDetails userDetails) {
		
		MeResponse response = authService.getMe(userDetails.getUserId());
		return ApiResponse.<MeResponse>builder()
		                  .success(true)
		                  .message("User profile fetched successfully")
		                  .data(response)
		                  .statusCode(200)
		                  .build();
	}
	
}
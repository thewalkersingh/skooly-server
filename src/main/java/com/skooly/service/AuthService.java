package com.skooly.service;

import com.skooly.dto.request.auth.*;
import com.skooly.dto.response.auth.*;

public interface AuthService {
	
	// ── Self Registration (PENDING → awaits admin approval) ───────────────────
	AuthMessageResponse register(RegisterRequest request);
	
	// ── Admin creates account directly (ACTIVE immediately) ───────────────────
	AuthMessageResponse createAccount(CreateAccountRequest request);
	
	// ── Admin approves or rejects pending registration ────────────────────────
	AuthMessageResponse approveAccount(Long userId);
	
	AuthMessageResponse rejectAccount(Long userId, String reason);
	
	// ── Login ─────────────────────────────────────────────────────────────────
	LoginResponse login(LoginRequest request);
	
	// ── OTP ───────────────────────────────────────────────────────────────────
	AuthMessageResponse verifyOtp(VerifyOtpRequest request);
	
	AuthMessageResponse resendOtp(ResendOtpRequest request);
	
	// ── First login — force password set ─────────────────────────────────────
	LoginResponse setPassword(SetPasswordRequest request);
	
	// ── Password reset ────────────────────────────────────────────────────────
	AuthMessageResponse forgotPassword(ForgotPasswordRequest request);
	
	AuthMessageResponse resetPassword(ResetPasswordRequest request);
	
	// ── Token management ──────────────────────────────────────────────────────
	LoginResponse refreshToken(String refreshToken);
	
	AuthMessageResponse logout(String refreshToken);
	
	// ── Current user ──────────────────────────────────────────────────────────
	MeResponse getMe(Long userId);
	
}
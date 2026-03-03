package com.skooly.service;
import com.skooly.dto.request.*;
import com.skooly.dto.response.AuthResponse;
import com.skooly.dto.response.UserResponse;

public interface AuthService {
	AuthResponse login(LoginRequest request);
	
	AuthResponse refreshToken(RefreshTokenRequest request);
	
	void logout(Long userId);
	
	void forgotPassword(ForgotPasswordRequest request);
	
	void resetPassword(ResetPasswordRequest request);
	
	void changePassword(Long userId, ChangePasswordRequest request);
	
	// User management
	UserResponse getCurrentUser(Long userId);
	
	UserResponse createUser(CreateUserRequest request);
	
	UserResponse updateUser(Long id, CreateUserRequest request);
	
	void deleteUser(Long id);
	
	void updateUserStatus(Long id, Boolean isActive);
}
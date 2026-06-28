package com.skooly.dto.response.auth;

import com.skooly.enums.UserRole;
import com.skooly.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// ── LoginResponse ─────────────────────────────────────────────────────────────
// Returned after successful login OR token refresh

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
	
	private String accessToken;
	private String refreshToken;
	private String tokenType;           // always "Bearer"
	private long accessTokenExpiresIn;  // milliseconds
	
	// User info — so frontend doesn't need a separate /me call after login
	private Long userId;
	private String firstName;
	private String lastName;
	private UserRole role;
	private UserStatus status;
	private Long roleEntityId;          // teacherId / studentId etc
	private boolean firstLogin;         // true → redirect to set-password page
	private String message;
	
}
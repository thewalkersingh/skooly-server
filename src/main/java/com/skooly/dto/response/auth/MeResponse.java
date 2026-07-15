package com.skooly.dto.response.auth;

// ── MeResponse ────────────────────────────────────────────────────────────────
// Current authenticated user profile

import com.skooly.enums.UserRole;
import com.skooly.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeResponse {
	
	private Long userId;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private UserRole userRole;
	private UserStatus userStatus;
	private Long roleEntityId;
	private Long schoolId;             // resolved from roleEntityId based on userRole
	
}
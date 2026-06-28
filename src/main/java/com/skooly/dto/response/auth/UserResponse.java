package com.skooly.dto.response.auth;

import com.skooly.enums.UserRole;
import com.skooly.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
	
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private UserRole role;
	private UserStatus status;
	private Long roleEntityId;
	private boolean firstLogin;
	private LocalDateTime lastLoginAt;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
}
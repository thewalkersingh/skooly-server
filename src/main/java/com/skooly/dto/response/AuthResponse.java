package com.skooly.dto.response;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
	private String token;
	private String refreshToken;
	private long expiresIn;
	private UserResponse user;
}
package com.skooly.dto.response;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParentResponse {
	private Long id;
	private Long userId;
	private String firstName;
	private String lastName;
	private String phone;
	private String email;
	private String address;
	private String occupation;
	private String relation;
	private LocalDateTime createdAt;
}
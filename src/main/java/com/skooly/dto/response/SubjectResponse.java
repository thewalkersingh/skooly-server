package com.skooly.dto.response;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectResponse {
	private Long id;
	private String name;
	private String code;
	private String description;
	private LocalDateTime createdAt;
}
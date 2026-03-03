package com.skooly.dto.response;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassResponse {
	private Long id;
	private String name;
	private Integer gradeLevel;
	private LocalDateTime createdAt;
}
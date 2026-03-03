package com.skooly.dto.response;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponse {
	private Long id;
	private String name;
	private String description;
	private Long headId;
	private String headName;
	private LocalDateTime createdAt;
}
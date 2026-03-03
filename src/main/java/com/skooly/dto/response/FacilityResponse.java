package com.skooly.dto.response;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacilityResponse {
	private Long id;
	private String name;
	private String description;
	private String location;
	private String status;
	private LocalDateTime createdAt;
}
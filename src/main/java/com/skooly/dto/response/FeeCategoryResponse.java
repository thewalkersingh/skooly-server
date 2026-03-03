package com.skooly.dto.response;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeCategoryResponse {
	private Long id;
	private String name;
	private BigDecimal amount;
	private String description;
	private LocalDateTime createdAt;
}
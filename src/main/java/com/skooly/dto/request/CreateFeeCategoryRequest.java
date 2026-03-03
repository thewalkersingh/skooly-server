package com.skooly.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFeeCategoryRequest {
	@NotBlank(message = "Category name is required")
	@Size(max = 100)
	private String name;
	
	@NotNull(message = "Amount is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
	private BigDecimal amount;
	private String description;
}
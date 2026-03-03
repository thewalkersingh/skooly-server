package com.skooly.dto.request;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePayrollRequest {
	@NotNull(message = "Staff ID is required")
	private Long staffId;
	
	@NotNull @Min(1) @Max(12)
	private Integer month;
	
	@NotNull @Min(2000)
	private Integer year;
	
	@NotNull @DecimalMin("0.0")
	private BigDecimal basicSalary;
	
	@DecimalMin("0.0")
	private BigDecimal allowances = BigDecimal.ZERO;
	
	@DecimalMin("0.0")
	private BigDecimal deductions = BigDecimal.ZERO;
	private LocalDate paidDate;
}
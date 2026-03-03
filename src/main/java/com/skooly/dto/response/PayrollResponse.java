package com.skooly.dto.response;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollResponse {
	private Long id;
	private Long staffId;
	private String staffName;
	private Integer month;
	private Integer year;
	private BigDecimal basicSalary;
	private BigDecimal allowances;
	private BigDecimal deductions;
	private BigDecimal netSalary;
	private LocalDate paidDate;
	private LocalDateTime createdAt;
}
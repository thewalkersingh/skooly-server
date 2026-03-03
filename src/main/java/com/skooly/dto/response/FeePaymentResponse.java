package com.skooly.dto.response;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeePaymentResponse {
	private Long id;
	private Long studentId;
	private String studentName;
	private Long feeStructureId;
	private String feeCategoryName;
	private BigDecimal amountPaid;
	private LocalDate paymentDate;
	private String paymentMode;
	private String transactionId;
	private String status;
	private LocalDateTime createdAt;
}
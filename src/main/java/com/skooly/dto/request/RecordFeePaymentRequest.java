package com.skooly.dto.request;
import com.skooly.model.FeePayment;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordFeePaymentRequest {
	@NotNull(message = "Student ID is required")
	private Long studentId;
	
	@NotNull(message = "Fee structure ID is required")
	private Long feeStructureId;
	
	@NotNull(message = "Amount paid is required")
	@DecimalMin(value = "0.0", inclusive = false)
	private BigDecimal amountPaid;
	
	@NotNull(message = "Payment date is required")
	private LocalDate paymentDate;
	
	@NotNull(message = "Payment mode is required")
	private FeePayment.PaymentMode paymentMode;
	private String transactionId;
	private FeePayment.PaymentStatus status = FeePayment.PaymentStatus.PAID;
}
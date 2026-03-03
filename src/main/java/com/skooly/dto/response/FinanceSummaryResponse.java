package com.skooly.dto.response;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinanceSummaryResponse {
	private BigDecimal totalCollected;
	private long totalPaid;
	private long totalPending;
	private long totalOverdue;
}
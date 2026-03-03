package com.skooly.dto.response;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookIssueResponse {
	private Long id;
	private Long bookId;
	private String bookTitle;
	private Long studentId;
	private String studentName;
	private LocalDate issueDate;
	private LocalDate dueDate;
	private LocalDate returnDate;
	private BigDecimal fine;
	private String status;
	private LocalDateTime createdAt;
}
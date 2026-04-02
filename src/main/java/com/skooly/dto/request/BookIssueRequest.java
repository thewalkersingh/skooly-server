package com.skooly.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookIssueRequest {
	@NotNull(message = "Book ID is required")
	private Long bookId;
	
	@NotNull(message = "Student ID is required")
	private Long studentId;
	
	@NotNull(message = "Issue date is required")
	private LocalDate issueDate;
	
	@NotNull(message = "Due date is required")
	private LocalDate dueDate;
	
}
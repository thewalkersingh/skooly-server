package com.skooly.dto.request;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BookRequest {
	@NotBlank(message = "Title is required")
	private String title;
	private String author;
	private String isbn;
	private String category;
	private String publisher;
	private Integer publishedYear;
	
	@Min(value = 1, message = "Total copies must be at least 1")
	private Integer totalCopies = 1;
	
}
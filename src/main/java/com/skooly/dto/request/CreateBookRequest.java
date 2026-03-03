package com.skooly.dto.request;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookRequest {
	@NotBlank(message = "Title is required")
	@Size(max = 255)
	private String title;
	
	@Size(max = 255)
	private String author;
	
	@Size(max = 50)
	private String isbn;
	
	@Size(max = 100)
	private String category;
	
	@Min(value = 1, message = "Total copies must be at least 1")
	private Integer totalCopies = 1;
	
	@Size(max = 255)
	private String publisher;
	private Integer publishedYear;
}
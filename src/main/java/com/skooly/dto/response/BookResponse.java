package com.skooly.dto.response;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {
	private Long id;
	private String title;
	private String author;
	private String isbn;
	private String category;
	private Integer totalCopies;
	private Integer availableCopies;
	private String publisher;
	private Integer publishedYear;
	private LocalDateTime createdAt;
}
package com.skooly.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Book extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 255)
	private String title;
	
	@Column(length = 255)
	private String author;
	
	@Column(unique = true, length = 50)
	private String isbn;
	
	@Column(length = 100)
	private String category;
	
	@Column(name = "total_copies")
	private Integer totalCopies = 1;
	
	@Column(name = "available_copies")
	private Integer availableCopies = 1;
	
	@Column(length = 255)
	private String publisher;
	
	@Column(name = "published_year")
	private Integer publishedYear;
}
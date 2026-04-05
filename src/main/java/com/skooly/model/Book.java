package com.skooly.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.Year;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "school_id", nullable = false)
	private School school;
	
	@Column(nullable = false, length = 300)
	private String title;
	
	@Column(length = 200)
	private String author;
	
	@Column(length = 50)
	private String isbn;
	
	@Column(length = 100)
	private String category;
	
	@Column(name = "total_copies", nullable = false)
	private Integer totalCopies = 1;
	
	@Column(name = "available_copies", nullable = false)
	private Integer availableCopies = 1;
	
	@Column(length = 200)
	private String publisher;
	
	@Column(name = "published_year")
	private Integer publishedYear;
	
}
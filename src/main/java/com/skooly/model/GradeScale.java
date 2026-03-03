package com.skooly.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "grade_scale")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class GradeScale {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 5)
	private String grade;
	
	@Column(name = "min_marks", nullable = false, precision = 5, scale = 2)
	private BigDecimal minMarks;
	
	@Column(name = "max_marks", nullable = false, precision = 5, scale = 2)
	private BigDecimal maxMarks;
	
	@Column(precision = 3, scale = 2)
	private BigDecimal gpa;
}
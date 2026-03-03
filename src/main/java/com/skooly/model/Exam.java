package com.skooly.model;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "exams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exam extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 150)
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "class_id", nullable = false)
	private SchoolClass schoolClass;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_id", nullable = false)
	private Subject subject;
	
	@Column(name = "exam_date", nullable = false)
	private LocalDate examDate;
	
	@Column(name = "total_marks", nullable = false, precision = 6, scale = 2)
	private BigDecimal totalMarks;
	
	@Column(name = "passing_marks", nullable = false, precision = 6, scale = 2)
	private BigDecimal passingMarks;
	
	@Column(name = "academic_year", nullable = false, length = 20)
	private String academicYear;
}
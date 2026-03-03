package com.skooly.model;// Result.java
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "results",
		uniqueConstraints = @UniqueConstraint(columnNames = {"exam_id", "student_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "exam_id", nullable = false)
	private Exam exam;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;
	
	@Column(name = "marks_obtained", nullable = false, precision = 6, scale = 2)
	private BigDecimal marksObtained;
	
	@Column(length = 5)
	private String grade;
	
	@Column(length = 255)
	private String remarks;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ResultStatus status;
	
	public enum ResultStatus {PASS, FAIL}
}
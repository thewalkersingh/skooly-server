package com.skooly.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "fee_structures")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class FeeStructure extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "class_id", nullable = false)
	private SchoolClass schoolClass;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fee_category_id", nullable = false)
	private FeeCategory feeCategory;
	
	@Column(name = "academic_year", nullable = false, length = 20)
	private String academicYear;
	
	@Column(name = "due_date")
	private LocalDate dueDate;
}
package com.skooly.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "classes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SchoolClass extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 100)
	private String name;
	
	@Column(name = "grade_level", nullable = false)
	private Integer gradeLevel;
}
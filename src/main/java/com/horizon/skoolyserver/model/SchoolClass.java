package com.horizon.skoolyserver.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "school_class")
public class SchoolClass {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String grade;   // e.g., "10"
	private String section; // e.g., "A"
	
	@ManyToOne
	@JoinColumn(name = "teacher_id")
	@JsonBackReference
	private Teacher teacher;
	
	@OneToMany(mappedBy = "schoolClass")
	@JsonBackReference
	private List<Student> students = new ArrayList<>();
}
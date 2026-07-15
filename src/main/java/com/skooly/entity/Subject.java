package com.skooly.entity;

import com.skooly.enums.SubjectStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "subjects")
public class Subject {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "subject_name", nullable = false, length = 100)
	private String subjectName;
	
	@Column(name = "subject_code", nullable = false, unique = true, length = 50)
	private String subjectCode;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private SubjectStatus subjectStatus;   // ACTIVE, INACTIVE, ELECTIVE, etc.
	
	// Subject owns the subject_teachers join table.
	// Teacher does NOT hold a back-reference list — removes the circular dependency.
	// To assign a teacher to a subject: load Subject, add Teacher to this list, save Subject.
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "subject_teachers",
		 joinColumns = @JoinColumn(name = "subject_id"),
		 inverseJoinColumns = @JoinColumn(name = "teacher_id"))
	@Builder.Default
	private List<Teacher> teachers = new ArrayList<>();
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@Column(nullable = false)
	private LocalDateTime updatedAt;
	
	@PrePersist
	public void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}
	
	@PreUpdate
	public void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
	
}
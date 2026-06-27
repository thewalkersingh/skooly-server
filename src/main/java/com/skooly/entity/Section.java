package com.skooly.entity;

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
@Table(
	name = "sections",
	uniqueConstraints = {@UniqueConstraint(columnNames = {"classroom_id", "section_name"})
	})
public class Section {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Integer capacity = 60;
	
	@Column(name = "section_name", nullable = false, length = 5)
	private String sectionName;   // e.g. "A", "B", "C"
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@Column(nullable = false)
	private LocalDateTime updatedAt;
	
	// Section belongs to a Classroom (insert Classroom first, then Section)
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "classroom_id", nullable = false)
	private Classroom classroom;
	
	// A section is assigned one class teacher (nullable — can be assigned later)
	// Teacher does NOT hold a back List<Section> — this breaks the cycle.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;
	
	// Section owns the section_subjects join table.
	// Subject does NOT hold a back List<Section>.
	// Insert Subjects first (they have no dependencies), then link them here.
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "section_subjects",
		joinColumns = @JoinColumn(name = "section_id"),
		inverseJoinColumns = @JoinColumn(name = "subject_id")
	)
	@Builder.Default
	private List<Subject> subjects = new ArrayList<>();
	
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
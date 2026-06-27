package com.skooly.entity;

import com.skooly.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "students")
public class Student {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "date_of_birth", nullable = false)
	private LocalDate dob;
	
	@Column(name = "admission_date")
	private LocalDate admissionDate;
	
	@Column(length = 500)
	private String photoUrl;
	
	@Embedded
	private Address address;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private StudentStatus studentStatus;
	// Student entity
	@Column(length = 100)
	private String guardianName;      // "City Foster Care" or "John Doe"
	
	@Column(length = 50)
	private String guardianRelation;  // "Foster Home", "Uncle", "Guardian"
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "section_id", nullable = false)
	private Section section;
	
	// Parent can be null (student may not have a registered parent yet)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Parent parent;
	
	@OneToOne(optional = false, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "identity_id", nullable = false)
	private UserIdentity identity;
	
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
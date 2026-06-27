package com.skooly.entity;

import com.skooly.enums.TeacherStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "teachers")
public class Teacher {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 200)
	private String qualification;
	
	@Column(name = "experience_years")
	private Integer experience;
	
	@Column(length = 500)
	private String photoUrl;
	
	@Column(name = "date_of_birth")
	private LocalDate dob;
	
	@Column(name = "joining_date")
	private LocalDate joiningDate;
	
	@Embedded
	private Address address;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TeacherStatus status;
	
	// Teacher belongs to a School (can be inserted without School existing first
	// if nullable, but best practice is to always assign school)
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "school_id", nullable = false)
	private School school;
	
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
	
	// Teacher entity
	@ManyToMany(mappedBy = "teachers", fetch = FetchType.LAZY)
	private List<Subject> subjects = new ArrayList<>();
	
}
package com.skooly.entity;

import com.skooly.enums.Department;
import com.skooly.enums.StaffRole;
import com.skooly.enums.StaffStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Embedded
	private Address address;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private Department department;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 100)
	private StaffRole staffRole;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private StaffStatus status;   // ACTIVE, INACTIVE, ON_LEAVE, TERMINATED
	
	@Column(name = "joining_date")
	private LocalDate joiningDate;        // same as Teacher — when did they join
	
	@Column(name = "date_of_birth")
	private LocalDate dob;                // same as Teacher
	
	@Column(length = 500)
	private String photoUrl;              // profile photo
	
	@Column(length = 200)
	private String qualification;         // relevant for LIBRARIAN, LAB_ASSISTANT etc
	
	@Column(name = "experience_years")
	private Integer experience;           // years of experience
	
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
	
}
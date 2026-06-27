package com.skooly.entity;

import com.skooly.enums.StaffRole;
import com.skooly.enums.StaffStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	@Column(nullable = false, length = 30)
	private StaffRole role;
	// e.g. ADMIN, ACCOUNTANT, LIBRARIAN, LAB_ASSISTANT, DRIVER, SECURITY
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private StaffStatus status;
	// ACTIVE, INACTIVE, ON_LEAVE, TERMINATED
	
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
package com.skooly.entity;

import com.skooly.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User { // will be used later for login and ADMIN uses
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "identity_id", nullable = false)
	private UserIdentity identity;      // links to existing identity
	
	@Column(nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role;              // TEACHER, STUDENT, PARENT, STAFF
	
	private Long roleEntityId;          // teacherId / studentId / parentId / staffId
	// resolved at runtime based on role
	
	private boolean isActive;
	private LocalDateTime lastLogin;
	private LocalDateTime createdAt;
	
}
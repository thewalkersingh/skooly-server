package com.skooly.entity;

import com.skooly.enums.UserRole;
import com.skooly.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "identity_id", nullable = false)
	private UserIdentity identity;
	
	@Column(nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private UserRole role;
	
	private Long roleEntityId;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private UserStatus status;
	
	private boolean firstLogin;         // ← renamed from isFirstLogin
	private String lastLoginDevice;
	private LocalDateTime lastLoginAt;
	
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
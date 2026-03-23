package com.skooly.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "school_id", nullable = false)
	private School school;
	
	@Column(nullable = false, length = 100)
	private String username;
	
	@Column(nullable = false, length = 255)
	private String password;
	
	@Column(nullable = false, length = 50)
	private String role;
	
	@Column(name = "is_active", nullable = false)
	private Boolean isActive = true;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();
}

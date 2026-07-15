package com.skooly.entity;

import com.skooly.enums.ParentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "parents")
public class Parent {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 200)
	private String occupation;
	
	@Column(length = 50)
	private String relation;
	
	@Embedded
	private Address address;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private ParentStatus parentStatus;
	
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
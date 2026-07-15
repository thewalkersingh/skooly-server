package com.skooly.entity;

import com.skooly.enums.ClassroomStatus;
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
@Table(name = "classrooms",
	uniqueConstraints = {@UniqueConstraint(columnNames = {"school_id", "classroom_code"})})
public class Classroom {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "classroom_name", nullable = false, length = 100)
	private String classroomName;
	
	@Column(name = "classroom_code", nullable = false, unique = true, length = 50)
	private String classroomCode;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private ClassroomStatus classroomStatus;   // ACTIVE, INACTIVE, etc.
	
	// Owning side: Classroom belongs to a School.
	// School does NOT hold a List<Classroom> — query instead.
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "school_id", nullable = false)
	private School school;
	
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
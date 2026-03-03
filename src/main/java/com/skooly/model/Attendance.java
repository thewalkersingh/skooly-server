package com.skooly.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "attendance",
		uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "date"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Attendance extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "class_id", nullable = false)
	private SchoolClass schoolClass;
	
	@Column(nullable = false)
	private LocalDate date;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AttendanceStatus status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "marked_by")
	private User markedBy;
	
	@Column(length = 255)
	private String remarks;
	
	public enum AttendanceStatus { PRESENT, ABSENT, LATE, HALF_DAY, HOLIDAY }
}
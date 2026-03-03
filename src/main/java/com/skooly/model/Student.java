package com.skooly.model;// Student.java
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", unique = true)
	private User user;
	
	@Column(name = "first_name", nullable = false, length = 100)
	private String firstName;
	
	@Column(name = "last_name", nullable = false, length = 100)
	private String lastName;
	private LocalDate dob;
	
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	@Column(columnDefinition = "TEXT")
	private String address;
	
	@Column(length = 20)
	private String phone;
	
	@Column(length = 150)
	private String email;
	
	@Column(name = "admission_date")
	private LocalDate admissionDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "class_id")
	private SchoolClass schoolClass;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "section_id")
	private Section section;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Parent parent;
	
	@Column(length = 255)
	private String photo;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private Status status = Status.ACTIVE;
	
	public enum Gender {MALE, FEMALE, OTHER}
	
	public enum Status {ACTIVE, INACTIVE}
}
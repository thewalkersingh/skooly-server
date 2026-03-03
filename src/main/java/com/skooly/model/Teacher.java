package com.skooly.model;// Teacher.java
import jakarta.persistence.*;
import lombok.*;

import javax.security.auth.Subject;
import java.time.LocalDate;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher extends BaseEntity {
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
	
	@Column(name = "joining_date")
	private LocalDate joiningDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_id")
	private Subject subject;
	
	@Column(length = 255)
	private String qualification;
	private Integer experience = 0;
	
	@Column(length = 255)
	private String photo;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private Status status = Status.ACTIVE;
	
	public enum Gender {MALE, FEMALE, OTHER}
	
	public enum Status {ACTIVE, INACTIVE}
}
package com.skooly.model;
import com.skooly.constant.Gender;
import com.skooly.constant.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "teachers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "school_id", nullable = false)
	private School school;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
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
	
	@Column(length = 100)
	private String email;
	
	@Column(name = "joining_date")
	private LocalDate joiningDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_id")
	private Subject subject;
	
	@Column(length = 200)
	private String qualification;
	private Integer experience;
	
	@Column(length = 500)
	private String photo;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status = Status.ACTIVE;
	
//	public enum Gender {MALE, FEMALE, OTHER}

//	public enum Status {ACTIVE, INACTIVE}
}

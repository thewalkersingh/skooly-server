package com.skooly.model;// Staff.java
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "staff")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff extends BaseEntity {
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id")
	private Department department;
	
	@Column(length = 100)
	private String designation;
	
	@Column(name = "joining_date")
	private LocalDate joiningDate;
	
	@Column(precision = 10, scale = 2)
	private BigDecimal salary;
	
	@Column(length = 255)
	private String photo;
	
	@Enumerated(EnumType.STRING)
	private StaffStatus status = StaffStatus.ACTIVE;
	
	public enum Gender {MALE, FEMALE, OTHER}
	
	public enum StaffStatus {ACTIVE, INACTIVE}
}
package com.skooly.entity;

import com.skooly.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_identity",
	 uniqueConstraints = {
		  @UniqueConstraint(columnNames = {"email"}),
		  @UniqueConstraint(columnNames = {"phone"})
	 })
public class UserIdentity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 100)
	private String firstName;
	
	@Column(length = 100)
	private String lastName;
	
	@Column(length = 30, unique = true, nullable = false)
	private String phone;
	
	@Column(length = 100, unique = true)
	private String email;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Gender gender;
	
}
package com.skooly.entity;

import com.skooly.enums.SchoolStatus;
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
@Table(name = "schools")
public class School {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 200)
	private String schoolName;
	
	@Column(nullable = false, unique = true, length = 50)
	private String schoolCode;
	
	@Column(nullable = false, length = 300)
	private String address;
	
	@Column(length = 20, unique = true)
	private String phone;
	
	@Column(length = 100, unique = true)
	private String email;
	
	@Column(length = 500)
	private String logoUrl;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private SchoolStatus status;
	
}
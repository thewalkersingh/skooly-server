package com.horizon.skoolyserver.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "teachers")
public class Teacher {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String employeeCode;
	private String name;
	private String email;
	private String phone;
	
	@OneToMany(mappedBy = "teacher")
	private List<SchoolClass> classes;
}
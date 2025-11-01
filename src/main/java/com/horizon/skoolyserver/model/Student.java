package com.horizon.skoolyserver.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "students")
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String admissionNumber;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String country;
	
	@ManyToOne
	@JoinColumn(name = "class_id")
	private SchoolClass schoolClass;
	
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Parent parent;
}

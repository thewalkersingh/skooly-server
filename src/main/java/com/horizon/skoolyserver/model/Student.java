package com.horizon.skoolyserver.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "admission_number")
	private String admissionNumber;
	private String firstName;
	private String lastName;
	private Long age;
	private String email;
	private String phone;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String country;
	
	@ManyToOne
	@JoinColumn(name = "class_id")
	@JsonBackReference
	private SchoolClass schoolClass;
	
	@ManyToOne
	@JoinColumn(name = "parent_id")
	@JsonBackReference
	private Parent parent;
}

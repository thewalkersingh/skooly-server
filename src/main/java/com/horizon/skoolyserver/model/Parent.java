package com.horizon.skoolyserver.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "parents")
public class Parent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	private String name;
	@Email
	private String email;
	private String phone;
	
	@OneToMany(mappedBy = "parent")
	@JsonManagedReference
	private List<Student> children=new ArrayList<Student>();
}
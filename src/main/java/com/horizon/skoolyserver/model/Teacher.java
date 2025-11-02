package com.horizon.skoolyserver.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.horizon.skoolyserver.constants.Subject;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "teachers")
public class Teacher {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String employeeCode;
	private String name;
	private String email;
	private String phone;
	
	@ElementCollection(targetClass = Subject.class) @Enumerated(EnumType.STRING)
	@CollectionTable(name = "teacher_subjects", joinColumns = @JoinColumn(name = "teacher_id"))
	@Column(name = "subject")
	private Set<Subject> subjects = new HashSet<>();
	
	@OneToMany(mappedBy = "teacher") @JsonManagedReference
	private List<SchoolClass> classes = new ArrayList<>();
}
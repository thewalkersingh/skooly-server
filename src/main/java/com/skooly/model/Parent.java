package com.skooly.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parent {
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
	
	@Column(length = 20)
	private String phone;
	
	@Column(length = 100)
	private String email;
	
	@Column(columnDefinition = "TEXT")
	private String address;
	
	@Column(length = 200)
	private String occupation;
	
	@Column(length = 50)
	private String relation;
	
}

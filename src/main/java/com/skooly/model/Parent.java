package com.skooly.model;// Parent.java
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parent extends BaseEntity {
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
	
	@Column(length = 20)
	private String phone;
	
	@Column(length = 150)
	private String email;
	
	@Column(columnDefinition = "TEXT")
	private String address;
	
	@Column(length = 150)
	private String occupation;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Relation relation;
	
	public enum Relation {FATHER, MOTHER, GUARDIAN}
}
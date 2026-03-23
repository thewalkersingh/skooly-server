package com.skooly.model;
import com.skooly.constant.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "schools")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class School {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 200)
	private String name;
	
	@Column(nullable = false, unique = true, length = 50)
	private String code;
	
	@Column(columnDefinition = "TEXT")
	private String address;
	
	@Column(length = 20)
	private String phone;
	
	@Column(length = 100)
	private String email;
	
	@Column(length = 500)
	private String logo;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status = Status.ACTIVE;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

//	public enum Status {ACTIVE, INACTIVE}
}

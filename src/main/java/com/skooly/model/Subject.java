package com.skooly.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subjects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "school_id", nullable = false)
	private School school;
	
	@Column(nullable = false, length = 100)
	private String name;
	
	@Column(length = 50)
	private String code;
	
	@Column(columnDefinition = "TEXT")
	private String description;
}

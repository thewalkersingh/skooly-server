package com.skooly.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "school_id", nullable = false)
	private School school;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "class_id", nullable = false)
	private SchoolClass schoolClass;
	
	@Column(nullable = false, length = 50)
	private String name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "teacher_id")
	private Teacher teacher;
	
	@Column(nullable = false)
	private Integer capacity = 40;
}

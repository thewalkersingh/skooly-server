package com.skooly.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "facilities")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Facility extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 100)
	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@Column(length = 255)
	private String location;
	
	@Enumerated(EnumType.STRING)
	private FacilityStatus status = FacilityStatus.ACTIVE;
	
	public enum FacilityStatus { ACTIVE, INACTIVE, UNDER_MAINTENANCE }
}
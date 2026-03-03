package com.skooly.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 100)
	private String name;
	
	@Enumerated(EnumType.STRING)
	private RoomType type;
	private Integer capacity;
	
	@Column(length = 50)
	private String floor;
	
	@Column(length = 100)
	private String building;
	
	@Enumerated(EnumType.STRING)
	private Status status = Status.AVAILABLE;
	
	public enum RoomType {CLASSROOM, LAB, LIBRARY, OFFICE, SPORTS, OTHER}
	
	public enum Status {AVAILABLE, OCCUPIED, UNDER_MAINTENANCE}
}
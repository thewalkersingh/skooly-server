package com.skooly.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "maintenance_logs")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MaintenanceLog extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "facility_id", nullable = false)
	private Facility facility;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reported_by")
	private User reportedBy;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String issue;
	
	@Column(name = "reported_date", nullable = false)
	private LocalDate reportedDate;
	
	@Column(name = "resolved_date")
	private LocalDate resolvedDate;
	
	@Enumerated(EnumType.STRING)
	private MaintenanceStatus status = MaintenanceStatus.OPEN;
	
	public enum MaintenanceStatus { OPEN, IN_PROGRESS, RESOLVED }
}
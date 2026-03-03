package com.skooly.model;// LeaveRequest.java
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "leave_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequest extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "staff_id", nullable = false)
	private Staff staff;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "leave_type", nullable = false)
	private LeaveType leaveType;
	
	@Column(name = "from_date", nullable = false)
	private LocalDate fromDate;
	
	@Column(name = "to_date", nullable = false)
	private LocalDate toDate;
	
	@Column(columnDefinition = "TEXT")
	private String reason;
	
	@Enumerated(EnumType.STRING)
	private LeaveStatus status = LeaveStatus.PENDING;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private User approvedBy;
	
	public enum LeaveType {SICK, CASUAL, EARNED, MATERNITY, OTHER}
	
	public enum LeaveStatus {PENDING, APPROVED, REJECTED}
}
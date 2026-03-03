package com.skooly.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Action action;
	
	@Column(nullable = false, length = 50)
	private String module;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	@Column(name = "ip_address", length = 50)
	private String ipAddress;
	
	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();
	
	public enum Action {CREATE, UPDATE, DELETE, LOGIN, LOGOUT}
}
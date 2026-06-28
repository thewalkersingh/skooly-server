package com.skooly.entity;

import com.skooly.enums.NotificationChannel;
import com.skooly.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "notifications")
public class Notification {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Long userId;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	private NotificationType type;
	
	@Column(nullable = false, length = 200)
	private String title;
	
	@Column(nullable = false, length = 500)
	private String message;
	
	private boolean isRead;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private NotificationChannel channel;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime sentAt;
	
	private LocalDateTime readAt;
	
	@PrePersist
	public void onCreate() {
		this.sentAt = LocalDateTime.now();
	}
	
}
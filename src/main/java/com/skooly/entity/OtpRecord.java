package com.skooly.entity;

import com.skooly.enums.OtpPurpose;
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
@Table(name = "otp_records")
public class OtpRecord {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;                      // which user this OTP belongs to
	
	@Column(nullable = false, length = 10)
	private String otp;                     // the OTP code e.g. "482910"
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private OtpPurpose purpose;             // FIRST_LOGIN, PASSWORD_RESET etc
	
	@Column(nullable = false)
	private LocalDateTime expiresAt;        // createdAt + 5 minutes
	
	private boolean used;                   // true = already verified, can't reuse
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@PrePersist
	public void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.expiresAt = this.createdAt.plusMinutes(5);
	}
	
}
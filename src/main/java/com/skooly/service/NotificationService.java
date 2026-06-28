package com.skooly.service;

import com.skooly.enums.NotificationChannel;
import com.skooly.enums.NotificationType;

// NotificationService — thin layer you expand later
public interface NotificationService {
	
	// ── Core send method ──────────────────────────────────────────────────────
	void send(Long userId, NotificationType type, String title, String message, NotificationChannel channel);
	
	// ── Convenience methods ───────────────────────────────────────────────────
	void sendEmail(String toEmail, String subject, String body);
	
	void sendSms(String toPhone, String message);
	
	// ── Auth specific helpers ─────────────────────────────────────────────────
	void sendOtp(String toEmail, String toPhone, String otp, NotificationChannel channel);
	
	void sendAccountApproved(String toEmail, String toPhone, String firstName);
	
	void sendAccountRejected(String toEmail, String toPhone, String firstName, String reason);
	
	void sendAccountCreated(String toEmail, String toPhone, String firstName, String tempPassword);
	
	void sendPasswordChanged(String toEmail, String toPhone, String firstName);
	
	void sendPendingApprovalToAdmin(String adminEmail, String adminPhone, String applicantName, String role);
	
}
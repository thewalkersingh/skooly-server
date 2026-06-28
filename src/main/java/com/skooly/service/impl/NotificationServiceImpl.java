package com.skooly.service.impl;

import com.skooly.entity.Notification;
import com.skooly.enums.NotificationChannel;
import com.skooly.enums.NotificationType;
import com.skooly.repository.NotificationRepository;
import com.skooly.service.NotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
	
	private final JavaMailSender mailSender;
	private final NotificationRepository notificationRepository;
	
	// ── Core send — saves to DB + dispatches to channel ──────────────────────
	@Override
	public void send(Long userId, NotificationType type, String title,
		String message, NotificationChannel channel) {
		// Save to DB — in-app notification record
		Notification notification = Notification.builder()
		                                        .userId(userId)
		                                        .type(type)
		                                        .title(title)
		                                        .message(message)
		                                        .channel(channel)
		                                        .isRead(false)
		                                        .build();
		notificationRepository.save(notification);
	}
	
	// ── Email ─────────────────────────────────────────────────────────────────
	@Async   // fire and forget — don't block the request thread
	@Override
	public void sendEmail(String toEmail, String subject, String body) {

		if (toEmail == null || toEmail.isBlank()) {
			log.warn("Skipping email — no email address provided");
			return;
		}
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setTo(toEmail);
			helper.setSubject(subject);
			helper.setText(body, true);   // true = HTML content
			helper.setFrom("anysignup47@gmail.com", "SkoolyTest");
			mailSender.send(message);
			log.info("Email sent to {}", toEmail);
		} catch (MessagingException e) {
			log.error("Failed to send email to {}: {}", toEmail, e.getMessage());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	// just for testing, actual method is above
//	@Override
//	public void sendEmail(String to, String subject, String body) {
//
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setTo(to);
//		message.setSubject(subject);
//		message.setText(body);
//		mailSender.send(message);
//	}
	
	// ── SMS (stub — replace with Twilio/AWS SNS later) ────────────────────────
	@Async
	@Override
	public void sendSms(String toPhone, String message) {
		
		if (toPhone == null || toPhone.isBlank()) {
			log.warn("Skipping SMS — no phone number provided");
			return;
		}
		// TODO: Replace with Twilio or AWS SNS integration
		// TwilioClient.messages.create(toPhone, from, message);
		log.info("[SMS STUB] To: {} | Message: {}", toPhone, message);
	}
	
	// ── Auth specific helpers ─────────────────────────────────────────────────
	@Override
	public void sendOtp(String toEmail, String toPhone, String otp, NotificationChannel channel) {
		
		String subject = "Your Skooly OTP Code";
		String body = buildOtpEmailBody(otp);
		String smsMessage = "Your Skooly OTP is: " + otp + ". Valid for 5 minutes. Do not share.";
		
		switch (channel) {
			case EMAIL -> sendEmail(toEmail, subject, body);
			case SMS -> sendSms(toPhone, smsMessage);
			case ALL, IN_APP -> {
				sendEmail(toEmail, subject, body);
				sendSms(toPhone, smsMessage);
			}
		}
	}
	
	@Override
	public void sendAccountApproved(String toEmail, String toPhone, String firstName) {
		
		String subject = "Your Skooly Account Has Been Approved";
		String body = buildSimpleEmailBody(
			"Account Approved 🎉",
			"Hi " + firstName + ",",
			"Your account request has been approved. You can now log in to Skooly.",
			"Log In Now", "http://localhost:5173/login"   // replace with real URL later
		);
		sendEmail(toEmail, subject, body);
		sendSms(toPhone, "Hi " + firstName + ", your Skooly account has been approved. You can now log in.");
	}
	
	@Override
	public void sendAccountRejected(String toEmail, String toPhone,
		String firstName, String reason) {
		
		String subject = "Your Skooly Account Request Was Rejected";
		String body = buildSimpleEmailBody(
			"Account Request Rejected",
			"Hi " + firstName + ",",
			"Unfortunately your account request has been rejected." +
				(reason != null ? "<br><br><b>Reason:</b> " + reason : ""),
			null, null
		);
		sendEmail(toEmail, subject, body);
		sendSms(toPhone, "Hi " + firstName + ", your Skooly account request was rejected."
			                 + (reason != null ? " Reason: " + reason : ""));
	}
	
	@Override
	public void sendAccountCreated(String toEmail, String toPhone,
		String firstName, String tempPassword) {
		
		String subject = "Welcome to Skooly — Your Account Is Ready";
		String body = buildSimpleEmailBody(
			"Welcome to Skooly!",
			"Hi " + firstName + ",",
			"Your account has been created by your school admin.<br><br>" +
				"<b>Temporary Password:</b> " + tempPassword + "<br><br>" +
				"Please log in and change your password immediately.",
			"Log In Now", "http://localhost:5173/login"
		);
		sendEmail(toEmail, subject, body);
		sendSms(toPhone, "Hi " + firstName + ", your Skooly account is ready. "
			                 + "Temp password: " + tempPassword + ". Please change it after login.");
	}
	
	@Override
	public void sendPasswordChanged(String toEmail, String toPhone, String firstName) {
		
		String subject = "Your Skooly Password Was Changed";
		String body = buildSimpleEmailBody(
			"Password Changed",
			"Hi " + firstName + ",",
			"Your password was successfully changed. If you did not make this change, " +
				"please contact your school admin immediately.",
			null, null
		);
		sendEmail(toEmail, subject, body);
		sendSms(toPhone, "Hi " + firstName + ", your Skooly password was changed. "
			                 + "If this wasn't you, contact your admin immediately.");
	}
	
	@Override
	public void sendPendingApprovalToAdmin(String adminEmail, String adminPhone,
		String applicantName, String role) {
		
		String subject = "New Account Request — Action Required";
		String body = buildSimpleEmailBody(
			"New Account Request",
			"Hello Admin,",
			"<b>" + applicantName + "</b> has submitted an account request for the role of " +
				"<b>" + role + "</b>.<br><br>Please log in to approve or reject this request.",
			"Review Request", "http://localhost:5173/admin/approvals"
		);
		sendEmail(adminEmail, subject, body);
		sendSms(adminPhone, "New account request from " + applicantName
			                    + " (" + role + "). Please review in the Skooly admin panel.");
	}
	
	// ── Email Template Builders ───────────────────────────────────────────────
	
	private String buildOtpEmailBody(String otp) {
		
		return """
			<div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px;">
			    <h2 style="color: #4F46E5;">Your Skooly OTP Code</h2>
			    <p>Use the following OTP to complete your action:</p>
			    <div style="font-size: 36px; font-weight: bold; letter-spacing: 8px;
			                color: #4F46E5; padding: 20px; background: #F5F3FF;
			                border-radius: 8px; text-align: center;">
			        %s
			    </div>
			    <p style="color: #666; margin-top: 16px;">
			        This OTP is valid for <b>5 minutes</b>. Do not share it with anyone.
			    </p>
			    <p style="color: #999; font-size: 12px;">If you did not request this OTP, ignore this email.</p>
			</div>
			""".formatted(otp);
	}
	
	private String buildSimpleEmailBody(String heading, String greeting,
		String content, String btnText, String btnUrl) {
		
		String button = (btnText != null && btnUrl != null)
			                ? """
			<a href="%s" style="display: inline-block; padding: 12px 24px;
			   background: #4F46E5; color: white; text-decoration: none;
			   border-radius: 6px; margin-top: 16px;">%s</a>
			""".formatted(btnUrl, btnText)
			                : "";
		
		return """
			<div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px;">
			    <h2 style="color: #4F46E5;">%s</h2>
			    <p>%s</p>
			    <p>%s</p>
			    %s
			    <hr style="margin-top: 32px; border: none; border-top: 1px solid #eee;">
			    <p style="color: #999; font-size: 12px;">
			        This is an automated message from Skooly. Please do not reply.
			    </p>
			</div>
			""".formatted(heading, greeting, content, button);
	}
	
}
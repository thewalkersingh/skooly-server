package com.skooly.controller;

import com.skooly.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
	
	private final NotificationService notificationService;
	
	@PostMapping("/test-email")
	public String testEmail(@RequestParam String to) {
		
		notificationService.sendEmail(to, "This is a Test Email", "Hello from SendGrid!");
		return "Email sent to " + to;
	}
	
}
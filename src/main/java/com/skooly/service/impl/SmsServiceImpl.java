package com.skooly.service.impl;

import com.skooly.service.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {
	
	@Value("${twilio.account-sid}")
	private String accountSid;
	
	@Value("${twilio.auth-token}")
	private String authToken;
	
	@Value("${twilio.phone-number}")
	private String fromNumber;
	
	@Value("${twilio.enabled:false}")
	private boolean enabled;
	
	@PostConstruct
	public void init() {
		
		if (enabled) {
			Twilio.init(accountSid, authToken);
			log.info("Twilio SMS service initialized");
		} else {
			log.warn("Twilio SMS is DISABLED — OTPs will only be logged");
		}
	}
	
	@Override
	public void sendOtp(String toPhone, String otp) {
		
		if (!enabled) {
			log.info("[SMS STUB] OTP {} → {}", otp, toPhone);
			return;
		}
		try {
			Message.creator(new PhoneNumber(toPhone), new PhoneNumber(fromNumber),
				"Your Skooly OTP is: " + otp + ". Valid for 10 minutes.").create();
			log.info("OTP SMS sent to {}", toPhone);
		} catch (Exception e) {
			log.error("Failed to send OTP SMS to {}: {}", toPhone, e.getMessage());
			throw new RuntimeException("SMS delivery failed", e);
		}
	}
	
}
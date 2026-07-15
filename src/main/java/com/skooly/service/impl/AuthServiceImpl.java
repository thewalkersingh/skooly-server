package com.skooly.service.impl;

import com.skooly.dto.request.auth.*;
import com.skooly.dto.response.auth.AuthMessageResponse;
import com.skooly.dto.response.auth.LoginResponse;
import com.skooly.dto.response.auth.MeResponse;
import com.skooly.entity.OtpRecord;
import com.skooly.entity.RefreshToken;
import com.skooly.entity.User;
import com.skooly.entity.UserIdentity;
import com.skooly.enums.*;
import com.skooly.repository.OtpRecordRepository;
import com.skooly.repository.RefreshTokenRepository;
import com.skooly.repository.UserRepository;
import com.skooly.security.JwtUtil;
import com.skooly.service.AuthService;
import com.skooly.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	
	private final UserRepository userRepository;
	private final OtpRecordRepository otpRecordRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final NotificationService notificationService;
	private final JwtUtil jwtUtil;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	
	@Value("${jwt.refresh-token-expiry}")
	private long refreshTokenExpiry;
	
	@Value("${otp.expiry-minutes}")
	private int otpExpiryMinutes;
	
	// ── Self Registration ─────────────────────────────────────────────────────
	@Override
	public AuthMessageResponse register(RegisterRequest request) {
		
		// Validate no duplicate phone/email
		validateUniqueIdentifiers(request.getPhone(), request.getEmail());
		
		// Create UserIdentity
		UserIdentity identity = UserIdentity.builder()
		                                    .firstName(request.getFirstName())
		                                    .lastName(request.getLastName())
		                                    .phone(request.getPhone())
		                                    .email(request.getEmail())
		                                    .gender(request.getGender())
		                                    .build();
		
		// Create User with PENDING status
		User user = User.builder()
		                .identity(identity)
		                .password(passwordEncoder.encode(UUID.randomUUID().toString())) // temp random password
		                .userRole(request.getUserRole())
		                .roleEntityId(request.getRoleEntityId()).userStatus(UserStatus.PENDING)
		                .firstLogin(true)
		                .build();
		
		userRepository.save(user);
		
		// Notify all ADMIN users about the pending request
		userRepository.findByUserRole(UserRole.ADMIN, Pageable.unpaged())
		              .forEach(admin -> notificationService.sendPendingApprovalToAdmin(
			              admin.getIdentity().getEmail(),
			              admin.getIdentity().getPhone(),
			              request.getFirstName() + " " + request.getLastName(), request.getUserRole().name()
		              ));
		
		// In-app notification record
		notificationService.send(
			user.getId(),
			NotificationType.ACCOUNT_PENDING_APPROVAL,
			"Registration Submitted",
			"Your account request has been submitted and is awaiting admin approval.",
			NotificationChannel.IN_APP
		);
		
		return AuthMessageResponse.builder()
		                          .success(true)
		                          .message("Registration successful. Your account is pending admin approval.")
		                          .build();
	}
	
	// ── Admin Creates Account ─────────────────────────────────────────────────
	@Override
	public AuthMessageResponse createAccount(CreateAccountRequest request) {
		
		validateUniqueIdentifiers(request.getPhone(), request.getEmail());
		
		// Generate temp password
		String tempPassword = generateTempPassword();
		
		UserIdentity identity = UserIdentity.builder()
		                                    .firstName(request.getFirstName())
		                                    .lastName(request.getLastName())
		                                    .phone(request.getPhone())
		                                    .email(request.getEmail())
		                                    .gender(request.getGender())
		                                    .build();
		
		User user = User.builder()
		                .identity(identity)
		                .password(passwordEncoder.encode(tempPassword)).userRole(request.getUserRole())
		                .roleEntityId(request.getRoleEntityId()).userStatus(UserStatus.ACTIVE)
		                .firstLogin(true)       // force password change on first login
		                .build();
		
		userRepository.save(user);
		
		// Send credentials via email + SMS
		notificationService.sendAccountCreated(
			request.getEmail(),
			request.getPhone(),
			request.getFirstName(),
			tempPassword
		);
		
		notificationService.send(
			user.getId(),
			NotificationType.ACCOUNT_CREATED,
			"Account Created",
			"Your Skooly account has been created by your school admin.",
			NotificationChannel.IN_APP
		);
		
		return AuthMessageResponse.builder()
		                          .success(true)
		                          .message("Account created successfully. Credentials sent to user.")
		                          .build();
	}
	
	// ── Admin Approve / Reject ────────────────────────────────────────────────
	public AuthMessageResponse approveAccount(Long userId) {
		
		User user = userRepository.findById(userId)
		                          .orElseThrow(() -> new RuntimeException("User not found"));
		
		if (user.getUserStatus() != UserStatus.PENDING) {
			throw new IllegalStateException("Only PENDING accounts can be approved");
		}
		
		// Generate temp password — user will change on first login
		String tempPassword = generateTempPassword();
		user.setPassword(passwordEncoder.encode(tempPassword));
		user.setUserStatus(UserStatus.ACTIVE);
		userRepository.save(user);
		
		notificationService.sendAccountApproved(
			user.getIdentity().getEmail(),
			user.getIdentity().getPhone(),
			user.getIdentity().getFirstName()
		);
		
		notificationService.send(
			user.getId(),
			NotificationType.ACCOUNT_APPROVED,
			"Account Approved",
			"Your account has been approved. You can now log in to Skooly.",
			NotificationChannel.IN_APP
		);
		
		return AuthMessageResponse.builder()
		                          .success(true)
		                          .message("Account approved successfully.")
		                          .build();
	}
	
	@Override
	public AuthMessageResponse rejectAccount(Long userId, String reason) {
		
		User user = userRepository.findById(userId)
		                          .orElseThrow(() -> new RuntimeException("User not found"));
		
		if (user.getUserStatus() != UserStatus.PENDING) {
			throw new IllegalStateException("Only PENDING accounts can be rejected");
		}
		
		user.setUserStatus(UserStatus.REJECTED);
		userRepository.save(user);
		
		notificationService.sendAccountRejected(
			user.getIdentity().getEmail(),
			user.getIdentity().getPhone(),
			user.getIdentity().getFirstName(),
			reason
		);
		
		notificationService.send(
			user.getId(),
			NotificationType.ACCOUNT_REJECTED,
			"Account Rejected",
			"Your account request was rejected." + (reason != null ? " Reason: " + reason : ""),
			NotificationChannel.IN_APP
		);
		
		return AuthMessageResponse.builder()
		                          .success(true)
		                          .message("Account rejected.")
		                          .build();
	}
	
	// ── Login ─────────────────────────────────────────────────────────────────
	@Override
	public LoginResponse login(LoginRequest request) {
		
		// Authenticate via Spring Security — throws if credentials wrong
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
				request.getIdentifier(),
				request.getPassword()
			)
		);
		
		// Load user
		User user = userRepository
			            .findByIdentityEmailOrIdentityPhone(request.getIdentifier(), request.getIdentifier())
			            .orElseThrow(() -> new RuntimeException("User not found"));
		
		// Check status
		if (user.getUserStatus() != UserStatus.ACTIVE) {
			throw new IllegalStateException("Account is not active. Status: " + user.getUserStatus());
		}
		
		// First login → send OTP, don't issue tokens yet
		if (user.isFirstLogin()) {
			generateAndSendOtp(user, OtpPurpose.FIRST_LOGIN);
			return LoginResponse.builder()
			                    .firstLogin(true)
			                    .userId(user.getId()).userRole(user.getUserRole())
			                    .message("First login detected. OTP sent to your email and phone.")
			                    .build();
		}
		
		// New device detection
		boolean isNewDevice = request.getDeviceFingerprint() != null
			                      && !request.getDeviceFingerprint().equals(user.getLastLoginDevice());
		
		if (isNewDevice) {
			generateAndSendOtp(user, OtpPurpose.NEW_DEVICE_LOGIN);
			return LoginResponse.builder()
			                    .firstLogin(false)
			                    .userId(user.getId()).userRole(user.getUserRole())
			                    .message("New device detected. OTP sent to your email and phone.")
			                    .build();
		}
		
		// Normal login → issue tokens
		return issueTokens(user, request.getDeviceFingerprint());
	}
	
	// ── OTP Verification ──────────────────────────────────────────────────────
	@Override
	public AuthMessageResponse verifyOtp(VerifyOtpRequest request) {
		
		User user = userRepository
			            .findByIdentityEmailOrIdentityPhone(request.getIdentifier(), request.getIdentifier())
			            .orElseThrow(() -> new RuntimeException("User not found"));
		
		OtpRecord otpRecord = otpRecordRepository
			                      .findLatestValidOtp(user.getId(), request.getPurpose())
			                      .orElseThrow(() -> new RuntimeException("OTP not found or expired"));
		
		if (!otpRecord.getOtp().equals(request.getOtp())) {
			throw new IllegalStateException("Invalid OTP");
		}
		
		// Mark OTP as used
		otpRecord.setUsed(true);
		otpRecordRepository.save(otpRecord);
		
		return AuthMessageResponse.builder()
		                          .success(true)
		                          .message("OTP verified successfully.")
		                          .build();
	}
	
	// ── Resend OTP ────────────────────────────────────────────────────────────
	@Override
	public AuthMessageResponse resendOtp(ResendOtpRequest request) {
		
		User user = userRepository
			            .findByIdentityEmailOrIdentityPhone(request.getIdentifier(), request.getIdentifier())
			            .orElseThrow(() -> new RuntimeException("User not found"));
		
		generateAndSendOtp(user, request.getPurpose());
		
		return AuthMessageResponse.builder()
		                          .success(true)
		                          .message("OTP resent successfully.")
		                          .build();
	}
	
	// ── Set Password (first login) ────────────────────────────────────────────
	@Override
	public LoginResponse setPassword(SetPasswordRequest request) {
		
		if (!request.getNewPassword().equals(request.getConfirmPassword())) {
			throw new IllegalArgumentException("Passwords do not match");
		}
		
		User user = userRepository
			            .findByIdentityEmailOrIdentityPhone(request.getIdentifier(), request.getIdentifier())
			            .orElseThrow(() -> new RuntimeException("User not found"));
		
		// Verify OTP first
		OtpRecord otpRecord = otpRecordRepository
			                      .findLatestValidOtp(user.getId(), OtpPurpose.FIRST_LOGIN)
			                      .orElseThrow(() -> new RuntimeException("OTP not found or expired"));
		
		if (!otpRecord.getOtp().equals(request.getOtp())) {
			throw new IllegalStateException("Invalid OTP");
		}
		
		// Mark OTP used + set password + clear firstLogin flag
		otpRecord.setUsed(true);
		otpRecordRepository.save(otpRecord);
		
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		user.setFirstLogin(false);
		userRepository.save(user);
		
		notificationService.sendPasswordChanged(
			user.getIdentity().getEmail(),
			user.getIdentity().getPhone(),
			user.getIdentity().getFirstName()
		);
		
		// Issue tokens — user is now fully logged in
		return issueTokens(user, null);
	}
	
	// ── Forgot Password ───────────────────────────────────────────────────────
	@Override
	public AuthMessageResponse forgotPassword(ForgotPasswordRequest request) {
		
		User user = userRepository
			            .findByIdentityEmailOrIdentityPhone(request.getIdentifier(), request.getIdentifier())
			            .orElseThrow(() -> new RuntimeException("User not found"));
		
		generateAndSendOtp(user, OtpPurpose.PASSWORD_RESET);
		
		return AuthMessageResponse.builder()
		                          .success(true)
		                          .message("OTP sent to your registered email and phone.")
		                          .build();
	}
	
	// ── Reset Password ────────────────────────────────────────────────────────
	@Override
	public AuthMessageResponse resetPassword(ResetPasswordRequest request) {
		
		if (!request.getNewPassword().equals(request.getConfirmPassword())) {
			throw new IllegalArgumentException("Passwords do not match");
		}
		
		User user = userRepository
			            .findByIdentityEmailOrIdentityPhone(request.getIdentifier(), request.getIdentifier())
			            .orElseThrow(() -> new RuntimeException("User not found"));
		
		OtpRecord otpRecord = otpRecordRepository
			                      .findLatestValidOtp(user.getId(), OtpPurpose.PASSWORD_RESET)
			                      .orElseThrow(() -> new RuntimeException("OTP not found or expired"));
		
		if (!otpRecord.getOtp().equals(request.getOtp())) {
			throw new IllegalStateException("Invalid OTP");
		}
		
		otpRecord.setUsed(true);
		otpRecordRepository.save(otpRecord);
		
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);
		
		// Revoke all refresh tokens — forces re-login on all devices
		refreshTokenRepository.revokeAllByUserId(user.getId());
		
		notificationService.sendPasswordChanged(
			user.getIdentity().getEmail(),
			user.getIdentity().getPhone(),
			user.getIdentity().getFirstName()
		);
		
		return AuthMessageResponse.builder()
		                          .success(true)
		                          .message("Password reset successfully. Please log in with your new password.")
		                          .build();
	}
	
	// ── Refresh Token ─────────────────────────────────────────────────────────
	@Override
	public LoginResponse refreshToken(String refreshToken) {
		
		RefreshToken stored = refreshTokenRepository.findByToken(refreshToken)
		                                            .orElseThrow(() -> new RuntimeException("Refresh token not found"));
		
		if (stored.isRevoked()) {
			throw new IllegalStateException("Refresh token has been revoked");
		}
		
		if (stored.getExpiresAt().isBefore(LocalDateTime.now())) {
			throw new IllegalStateException("Refresh token has expired");
		}
		
		User user = stored.getUser();
		
		if (!jwtUtil.isRefreshTokenValid(refreshToken, user.getId())) {
			throw new IllegalStateException("Invalid refresh token");
		}
		
		// Rotate — revoke old, issue new
		stored.setRevoked(true);
		refreshTokenRepository.save(stored);
		
		return issueTokens(user, user.getLastLoginDevice());
	}
	
	// ── Logout ────────────────────────────────────────────────────────────────
	@Override
	public AuthMessageResponse logout(String refreshToken) {
		
		refreshTokenRepository.findByToken(refreshToken)
		                      .ifPresent(token -> {
			                      token.setRevoked(true);
			                      refreshTokenRepository.save(token);
		                      });
		
		return AuthMessageResponse.builder()
		                          .success(true)
		                          .message("Logged out successfully.")
		                          .build();
	}
	
	// ── Get Me ────────────────────────────────────────────────────────────────
	@Override
	@Transactional(readOnly = true)
	public MeResponse getMe(Long userId) {
		
		User user = userRepository.findById(userId)
		                          .orElseThrow(() -> new RuntimeException("User not found"));
		
		return MeResponse.builder()
		                 .userId(user.getId())
		                 .firstName(user.getIdentity().getFirstName())
		                 .lastName(user.getIdentity().getLastName())
		                 .email(user.getIdentity().getEmail())
		                 .phone(user.getIdentity().getPhone()).userRole(user.getUserRole())
		                 .userStatus(user.getUserStatus())
		                 .roleEntityId(user.getRoleEntityId())
		                 .build();
	}
	
	// ── Private Helpers ───────────────────────────────────────────────────────
	private LoginResponse issueTokens(User user, String deviceFingerprint) {
		
		String accessToken = jwtUtil.generateAccessToken(user);
		String refreshToken = jwtUtil.generateRefreshToken(user);
		
		// Save refresh token to DB
		RefreshToken token = RefreshToken.builder()
		                                 .token(refreshToken)
		                                 .user(user)
		                                 .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenExpiry / 1000))
		                                 .revoked(false)
		                                 .build();
		refreshTokenRepository.save(token);
		
		// Update last login info
		user.setLastLoginAt(LocalDateTime.now());
		if (deviceFingerprint != null) {
			user.setLastLoginDevice(deviceFingerprint);
		}
		userRepository.save(user);
		
		return LoginResponse.builder()
		                    .accessToken(accessToken)
		                    .refreshToken(refreshToken)
		                    .tokenType("Bearer")
		                    .accessTokenExpiresIn(refreshTokenExpiry)
		                    .userId(user.getId())
		                    .firstName(user.getIdentity().getFirstName())
		                    .lastName(user.getIdentity().getLastName()).userRole(user.getUserRole())
		                    .userStatus(user.getUserStatus())
		                    .roleEntityId(user.getRoleEntityId())
		                    .firstLogin(user.isFirstLogin())
		                    .build();
	}
	
	private void generateAndSendOtp(User user, OtpPurpose purpose) {
		// Invalidate previous OTPs for same purpose
		otpRecordRepository.invalidatePreviousOtps(user.getId(), purpose);
		
		// Generate 6-digit OTP
		String otp = String.format("%06d", new SecureRandom().nextInt(999999));
		
		// Save OTP record
		OtpRecord record = OtpRecord.builder()
		                            .user(user)
		                            .otp(otp)
		                            .purpose(purpose)
		                            .used(false)
		                            .build();
		otpRecordRepository.save(record);
		
		// Send via both channels
		notificationService.sendOtp(
			user.getIdentity().getEmail(),
			user.getIdentity().getPhone(),
			otp,
			NotificationChannel.ALL
		);
	}
	
	private void validateUniqueIdentifiers(String phone, String email) {
		
		if (userRepository.existsByIdentityPhone(phone)) {
			throw new IllegalStateException("Phone number already registered");
		}
		if (email != null && userRepository.existsByIdentityEmail(email)) {
			throw new IllegalStateException("Email already registered");
		}
	}
	
	private String generateTempPassword() {
		// 8 char alphanumeric temp password
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder(8);
		for (int i = 0; i < 8; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		return sb.toString();
	}
	
}
package com.skooly.service.impl;
import com.skooly.dto.request.*;
import com.skooly.dto.response.AuthResponse;
import com.skooly.dto.response.UserResponse;
import com.skooly.exception.BadRequestException;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.exception.UnauthorizedException;
import com.skooly.model.ActivityLog;
import com.skooly.model.Role;
import com.skooly.model.User;
import com.skooly.repository.RoleRepository;
import com.skooly.repository.UserRepository;
import com.skooly.security.JwtTokenProvider;
import com.skooly.security.PasswordResetTokenStore;
import com.skooly.security.UserPrincipal;
import com.skooly.service.ActivityLogService;
import com.skooly.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final PasswordResetTokenStore resetTokenStore;
	private final ActivityLogService activityLogService;
	
	@Override
	public AuthResponse login(LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
		                                                                  );
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtTokenProvider.generateToken(authentication);
		String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);
		
		UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
		User user = findUserById(principal.getId());
		
		// Log login activity
		activityLogService.log(user.getId(), ActivityLog.Action.LOGIN, "AUTH",
		                       "User logged in: "+user.getUsername(), null);
		
		return AuthResponse.builder()
				       .token(token)
				       .refreshToken(refreshToken)
				       .expiresIn(jwtTokenProvider.getExpirationMs())
				       .user(mapToUserResponse(user))
				       .build();
	}
	
	@Override
	public AuthResponse refreshToken(RefreshTokenRequest request) {
		if(!jwtTokenProvider.validateToken(request.getRefreshToken())){
			throw new UnauthorizedException("Invalid or expired refresh token");
		}
		Long userId = jwtTokenProvider.getUserIdFromToken(request.getRefreshToken());
		User user = findUserById(userId);
		
		if(!user.getIsActive()){
			throw new UnauthorizedException("User account is deactivated");
		}
		
		String newToken = jwtTokenProvider.generateTokenFromUserId(userId);
		
		return AuthResponse.builder()
				       .token(newToken)
				       .refreshToken(request.getRefreshToken())
				       .expiresIn(jwtTokenProvider.getExpirationMs())
				       .user(mapToUserResponse(user))
				       .build();
	}
	
	@Override
	public void logout(Long userId) {
		activityLogService.log(userId, ActivityLog.Action.LOGOUT, "AUTH",
		                       "User logged out", null);
		SecurityContextHolder.clearContext();
	}
	
	@Override
	public void forgotPassword(ForgotPasswordRequest request) {
		User user = userRepository.findByUsername(request.getUsername())
				            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		String token = resetTokenStore.generateToken(user.getUsername());
		
		// In production: send email with reset link containing token
		// For now: log the token (replace with email service)
		System.out.println("Password reset token for "+user.getUsername()+": "+token);
	}
	
	@Override
	public void resetPassword(ResetPasswordRequest request) {
		String username = resetTokenStore.getUsernameByToken(request.getToken());
		if(username == null){
			throw new BadRequestException("Invalid or expired reset token");
		}
		User user = userRepository.findByUsername(username)
				            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);
		resetTokenStore.invalidateToken(request.getToken());
	}
	
	@Override
	public void changePassword(Long userId, ChangePasswordRequest request) {
		User user = findUserById(userId);
		
		if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
			throw new BadRequestException("Current password is incorrect");
		}
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserResponse getCurrentUser(Long userId) {
		return mapToUserResponse(findUserById(userId));
	}
	
	@Override
	public UserResponse createUser(CreateUserRequest request) {
		if(userRepository.existsByUsername(request.getUsername())){
			throw new BadRequestException("Username '"+request.getUsername()+"' already exists");
		}
		Role role = roleRepository.findByName(request.getRole())
				            .orElseThrow(() -> new ResourceNotFoundException("Role not found: "+request.getRole()));
		
		User user = User.builder()
				            .username(request.getUsername())
				            .password(passwordEncoder.encode(request.getPassword()))
				            .role(role)
				            .isActive(request.getIsActive() != null ? request.getIsActive() : true)
				            .build();
		
		return mapToUserResponse(userRepository.save(user));
	}
	
	@Override
	public UserResponse updateUser(Long id, CreateUserRequest request) {
		User user = findUserById(id);
		if(request.getPassword() != null){
			user.setPassword(passwordEncoder.encode(request.getPassword()));
		}
		if(request.getRole() != null){
			Role role = roleRepository.findByName(request.getRole())
					            .orElseThrow(() -> new ResourceNotFoundException("Role not found: "+request.getRole()));
			user.setRole(role);
		}
		if(request.getIsActive() != null){
			user.setIsActive(request.getIsActive());
		}
		return mapToUserResponse(userRepository.save(user));
	}
	
	@Override
	public void deleteUser(Long id) {
		if(!userRepository.existsById(id)){
			throw new ResourceNotFoundException("User not found with id: "+id);
		}
		userRepository.deleteById(id);
	}
	
	@Override
	public void updateUserStatus(Long id, Boolean isActive) {
		User user = findUserById(id);
		user.setIsActive(isActive);
		userRepository.save(user);
	}
	
	// ── Private helpers ──────────────────────────────────────────────────────
	
	private User findUserById(Long id) {
		return userRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+id));
	}
	
	private UserResponse mapToUserResponse(User user) {
		return UserResponse.builder()
				       .id(user.getId())
				       .username(user.getUsername())
				       .role(user.getRole().getName())
				       .isActive(user.getIsActive())
				       .createdAt(user.getCreatedAt())
				       .build();
	}
}
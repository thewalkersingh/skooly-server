package com.skooly.service.impl;

import com.skooly.dto.response.auth.UserResponse;
import com.skooly.entity.User;
import com.skooly.enums.UserRole;
import com.skooly.enums.UserStatus;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.repository.UserRepository;
import com.skooly.service.UserService;
import com.skooly.wrapper.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	@Override
	@Transactional(readOnly = true)
	public UserResponse getUser(Long userId) {
		
		return userRepository.findById(userId)
		                     .map(this::toResponse)
		                     .orElseThrow(() -> new ResourceNotFoundException("User", userId));
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserResponse getUserByPhone(String phone) {
		
		return userRepository.findByIdentityPhone(phone)
		                     .map(this::toResponse)
		                     .orElseThrow(() -> new ResourceNotFoundException("User", "phone", phone));
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserResponse getUserByEmail(String email) {
		
		return userRepository.findByIdentityEmail(email)
		                     .map(this::toResponse)
		                     .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
	}
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	@Override
	@Transactional(readOnly = true)
	public PageResponse<UserResponse> getAllUsers(Pageable pageable) {
		
		Page<User> page = userRepository.findAll(pageable);
		return toPageResponse(page);
	}
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<UserResponse> getUsersByRole(UserRole role, Pageable pageable) {
		
		Page<User> page = userRepository.findByUserRole(role, pageable);
		return toPageResponse(page);
	}
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<UserResponse> getUsersByStatus(UserStatus status, Pageable pageable) {
		
		Page<User> page = userRepository.findByUserStatus(status, pageable);
		return toPageResponse(page);
	}
	
	// ── Pending approvals ─────────────────────────────────────────────────────
	@Override
	@Transactional(readOnly = true)
	public List<UserResponse> getPendingApprovals() {
		
		return userRepository.findAllPendingApprovals()
		                     .stream()
		                     .map(this::toResponse)
		                     .toList();
	}
	
	// ── Status management ─────────────────────────────────────────────────────
	@Override
	public UserResponse updateStatus(Long userId, UserStatus status) {
		
		User user = userRepository.findById(userId)
		                          .orElseThrow(() -> new ResourceNotFoundException("User", userId));
		user.setUserStatus(status);
		User saved = userRepository.save(user);
		return toResponse(saved);
	}
	
	// ── Soft delete ───────────────────────────────────────────────────────────
	@Override
	public void deleteUser(Long userId) {
		
		User user = userRepository.findById(userId)
		                          .orElseThrow(() -> new ResourceNotFoundException("User", userId));
		user.setUserStatus(UserStatus.DELETED);
		userRepository.save(user);
	}
	
	// ── Private helpers ───────────────────────────────────────────────────────
	// No mapper needed — UserResponse is simple enough to map manually
	// Avoids creating a UserMapper that would need UserIdentityMapper dependency
	private UserResponse toResponse(User user) {
		
		return UserResponse.builder()
		                   .id(user.getId())
		                   .firstName(user.getIdentity().getFirstName())
		                   .lastName(user.getIdentity().getLastName())
		                   .email(user.getIdentity().getEmail())
		                   .phone(user.getIdentity().getPhone()).userRole(user.getUserRole())
		                   .userStatus(user.getUserStatus())
		                   .roleEntityId(user.getRoleEntityId())
		                   .firstLogin(user.isFirstLogin())
		                   .lastLoginAt(user.getLastLoginAt())
		                   .createdAt(user.getCreatedAt())
		                   .updatedAt(user.getUpdatedAt())
		                   .build();
	}
	
	private PageResponse<UserResponse> toPageResponse(Page<User> page) {
		
		return PageResponse.<UserResponse>builder()
		                   .data(page.getContent().stream().map(this::toResponse).toList())
		                   .page(page.getNumber())
		                   .size(page.getSize())
		                   .totalElements(page.getTotalElements())
		                   .totalPages(page.getTotalPages())
		                   .hasNext(page.hasNext())
		                   .hasPrevious(page.hasPrevious())
		                   .build();
	}
	
}
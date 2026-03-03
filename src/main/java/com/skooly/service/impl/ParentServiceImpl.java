package com.skooly.service.impl;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateParentRequest;
import com.skooly.dto.request.SendNotificationRequest;
import com.skooly.dto.request.UpdateParentRequest;
import com.skooly.dto.response.NotificationResponse;
import com.skooly.dto.response.ParentResponse;
import com.skooly.dto.response.StudentSummaryResponse;
import com.skooly.exception.BadRequestException;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.mapper.ParentMapper;
import com.skooly.mapper.StudentMapper;
import com.skooly.model.Notification;
import com.skooly.model.Parent;
import com.skooly.model.User;
import com.skooly.repository.*;
import com.skooly.service.ParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ParentServiceImpl implements ParentService {
	private final ParentRepository parentRepository;
	private final StudentRepository studentRepository;
	private final UserRepository userRepository;
	private final NotificationRepository notificationRepository;
	private final ParentMapper parentMapper;
	private final StudentMapper studentMapper;
	
	// ── Parents ──────────────────────────────────────────────────────────────
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<ParentResponse> getAllParents(int page, int size, String search) {
		Pageable pageable = PageRequest.of(page-1, size, Sort.by("firstName").ascending());
		Page<Parent> parents = parentRepository.findWithFilters(search, pageable);
		List<ParentResponse> data = parents.getContent().stream().map(parentMapper::toResponse).toList();
		return new PageResponse<>(data, page, size, parents.getTotalElements(), parents.getTotalPages());
	}
	
	@Override
	@Transactional(readOnly = true)
	public ParentResponse getParentById(Long id) {
		return parentMapper.toResponse(findParentById(id));
	}
	
	@Override
	@Transactional(readOnly = true)
	public ParentResponse getMyProfile(Long userId) {
		Parent parent = parentRepository.findByUserId(userId)
				                .orElseThrow(() -> new ResourceNotFoundException("Parent profile not found"));
		return parentMapper.toResponse(parent);
	}
	
	@Override
	public ParentResponse updateMyProfile(Long userId, UpdateParentRequest request) {
		Parent parent = parentRepository.findByUserId(userId)
				                .orElseThrow(() -> new ResourceNotFoundException("Parent profile not found"));
		applyUpdates(parent, request);
		return parentMapper.toResponse(parentRepository.save(parent));
	}
	
	@Override
	public ParentResponse createParent(CreateParentRequest request) {
		if(request.getEmail() != null && parentRepository.existsByEmail(request.getEmail())){
			throw new BadRequestException("Email already in use");
		}
		User user = userRepository.findById(request.getUserId())
				            .orElseThrow(
						            () -> new ResourceNotFoundException("User not found with id: "+request.getUserId()));
		
		Parent parent = parentMapper.toEntity(request);
		parent.setUser(user);
		return parentMapper.toResponse(parentRepository.save(parent));
	}
	
	@Override
	public ParentResponse updateParent(Long id, UpdateParentRequest request) {
		Parent parent = findParentById(id);
		applyUpdates(parent, request);
		return parentMapper.toResponse(parentRepository.save(parent));
	}
	
	@Override
	public void deleteParent(Long id) {
		if(!parentRepository.existsById(id)){
			throw new ResourceNotFoundException("Parent not found with id: "+id);
		}
		parentRepository.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<StudentSummaryResponse> getChildrenByParent(Long parentId) {
		return studentRepository.findByParentId(parentId, Pageable.unpaged())
				       .getContent().stream().map(studentMapper::toSummaryResponse).toList();
	}
	
	// ── Notifications ────────────────────────────────────────────────────────
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<NotificationResponse> getMyNotifications(Long userId, int page, int size) {
		Pageable pageable = PageRequest.of(page-1, size);
		Page<Notification> notifications = notificationRepository
				                                   .findByUserIdOrderByCreatedAtDesc(userId, pageable);
		List<NotificationResponse> data = notifications.getContent()
				                                  .stream().map(parentMapper::toNotificationResponse).toList();
		return new PageResponse<>(data, page, size, notifications.getTotalElements(), notifications.getTotalPages());
	}
	
	@Override
	public NotificationResponse markAsRead(Long notificationId) {
		Notification notification = notificationRepository.findById(notificationId)
				                            .orElseThrow(() -> new ResourceNotFoundException(
						                            "Notification not found with id: "+notificationId));
		notification.setIsRead(true);
		return parentMapper.toNotificationResponse(notificationRepository.save(notification));
	}
	
	@Override
	public void markAllAsRead(Long userId) {
		notificationRepository.markAllAsReadByUserId(userId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public long getUnreadCount(Long userId) {
		return notificationRepository.countByUserIdAndIsReadFalse(userId);
	}
	
	@Override
	public List<NotificationResponse> sendNotification(SendNotificationRequest request) {
		return request.getUserIds().stream().map(userId -> {
			User user = userRepository.findById(userId)
					            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+userId));
			Notification notification = Notification.builder()
					                            .user(user)
					                            .title(request.getTitle())
					                            .message(request.getMessage())
					                            .isRead(false)
					                            .build();
			return parentMapper.toNotificationResponse(notificationRepository.save(notification));
		}).toList();
	}
	
	@Override
	public void deleteNotification(Long id) {
		if(!notificationRepository.existsById(id)){
			throw new ResourceNotFoundException("Notification not found with id: "+id);
		}
		notificationRepository.deleteById(id);
	}
	
	// ── Private helpers ──────────────────────────────────────────────────────
	
	private Parent findParentById(Long id) {
		return parentRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Parent not found with id: "+id));
	}
	
	private void applyUpdates(Parent parent, UpdateParentRequest request) {
		if(request.getFirstName() != null)
			parent.setFirstName(request.getFirstName());
		if(request.getLastName() != null)
			parent.setLastName(request.getLastName());
		if(request.getPhone() != null)
			parent.setPhone(request.getPhone());
		if(request.getEmail() != null)
			parent.setEmail(request.getEmail());
		if(request.getAddress() != null)
			parent.setAddress(request.getAddress());
		if(request.getOccupation() != null)
			parent.setOccupation(request.getOccupation());
		if(request.getRelation() != null)
			parent.setRelation(request.getRelation());
	}
}
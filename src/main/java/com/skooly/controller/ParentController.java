package com.skooly.controller;
import com.skooly.dto.common.ApiResponse;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateParentRequest;
import com.skooly.dto.request.SendNotificationRequest;
import com.skooly.dto.request.UpdateParentRequest;
import com.skooly.dto.response.NotificationResponse;
import com.skooly.dto.response.ParentResponse;
import com.skooly.dto.response.StudentSummaryResponse;
import com.skooly.security.UserPrincipal;
import com.skooly.service.ParentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ParentController {
	private final ParentService parentService;
	
	// ── Parents ──────────────────────────────────────────────────────────────
	
	@GetMapping("/parents")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<PageResponse<ParentResponse>>> getAllParents(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String search) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Parents fetched successfully",
		                                           parentService.getAllParents(page, size, search)));
	}
	
	@GetMapping("/parents/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'PARENT')")
	public ResponseEntity<ApiResponse<ParentResponse>> getParentById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Parent fetched successfully",
		                                           parentService.getParentById(id)));
	}
	
	@GetMapping("/parents/me")
	@PreAuthorize("hasRole('PARENT')")
	public ResponseEntity<ApiResponse<ParentResponse>> getMyProfile(
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Profile fetched successfully",
		                                           parentService.getMyProfile(userPrincipal.getId())));
	}
	
	@PutMapping("/parents/me")
	@PreAuthorize("hasRole('PARENT')")
	public ResponseEntity<ApiResponse<ParentResponse>> updateMyProfile(
			@AuthenticationPrincipal UserPrincipal userPrincipal,
			@Valid @RequestBody UpdateParentRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Profile updated successfully",
		                                           parentService.updateMyProfile(userPrincipal.getId(), request)));
	}
	
	@GetMapping("/parents/{id}/children")
	@PreAuthorize("hasAnyRole('ADMIN', 'PARENT')")
	public ResponseEntity<ApiResponse<List<StudentSummaryResponse>>> getChildren(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Children fetched successfully",
		                                           parentService.getChildrenByParent(id)));
	}
	
	@PostMapping("/parents")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<ParentResponse>> createParent(
			@Valid @RequestBody CreateParentRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Parent created successfully",
		                                                         parentService.createParent(request)));
	}
	
	@PutMapping("/parents/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<ParentResponse>> updateParent(
			@PathVariable Long id, @Valid @RequestBody UpdateParentRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Parent updated successfully",
		                                           parentService.updateParent(id, request)));
	}
	
	@DeleteMapping("/parents/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteParent(@PathVariable Long id) {
		parentService.deleteParent(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Parent deleted successfully", null));
	}
	
	// ── Notifications ────────────────────────────────────────────────────────
	
	@GetMapping("/notifications/me")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>> getMyNotifications(
			@AuthenticationPrincipal UserPrincipal userPrincipal,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Notifications fetched successfully",
		                                           parentService.getMyNotifications(userPrincipal.getId(), page, size)));
	}
	
	@GetMapping("/notifications/me/unread-count")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponse<Long>> getUnreadCount(
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Unread count fetched successfully",
		                                           parentService.getUnreadCount(userPrincipal.getId())));
	}
	
	@PatchMapping("/notifications/{id}/read")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Notification marked as read",
		                                           parentService.markAsRead(id)));
	}
	
	@PatchMapping("/notifications/read-all")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<ApiResponse<Void>> markAllAsRead(
			@AuthenticationPrincipal UserPrincipal userPrincipal) {
		parentService.markAllAsRead(userPrincipal.getId());
		return ResponseEntity.ok(new ApiResponse<>(true, "All notifications marked as read", null));
	}
	
	@PostMapping("/notifications")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<List<NotificationResponse>>> sendNotification(
			@Valid @RequestBody SendNotificationRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Notifications sent successfully",
		                                                         parentService.sendNotification(request)));
	}
	
	@DeleteMapping("/notifications/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable Long id) {
		parentService.deleteNotification(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Notification deleted successfully", null));
	}
}
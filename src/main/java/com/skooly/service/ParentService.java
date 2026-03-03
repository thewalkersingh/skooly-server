package com.skooly.service;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateParentRequest;
import com.skooly.dto.request.SendNotificationRequest;
import com.skooly.dto.request.UpdateParentRequest;
import com.skooly.dto.response.NotificationResponse;
import com.skooly.dto.response.ParentResponse;
import com.skooly.dto.response.StudentSummaryResponse;

import java.util.List;

public interface ParentService {
	// Parents
	PageResponse<ParentResponse> getAllParents(int page, int size, String search);
	
	ParentResponse getParentById(Long id);
	
	ParentResponse getMyProfile(Long userId);
	
	ParentResponse updateMyProfile(Long userId, UpdateParentRequest request);
	
	ParentResponse createParent(CreateParentRequest request);
	
	ParentResponse updateParent(Long id, UpdateParentRequest request);
	
	void deleteParent(Long id);
	
	List<StudentSummaryResponse> getChildrenByParent(Long parentId);
	
	// Notifications
	PageResponse<NotificationResponse> getMyNotifications(Long userId, int page, int size);
	
	NotificationResponse markAsRead(Long notificationId);
	
	void markAllAsRead(Long userId);
	
	long getUnreadCount(Long userId);
	
	List<NotificationResponse> sendNotification(SendNotificationRequest request);
	
	void deleteNotification(Long id);
}
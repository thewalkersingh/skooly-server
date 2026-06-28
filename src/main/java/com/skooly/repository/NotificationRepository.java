package com.skooly.repository;

import com.skooly.entity.Notification;
import com.skooly.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	
	// ── By User ───────────────────────────────────────────────────────────────
	Page<Notification> findByUserId(Long userId, Pageable pageable);
	
	List<Notification> findByUserIdAndIsRead(Long userId, boolean isRead);
	
	// ── Unread count ──────────────────────────────────────────────────────────
	long countByUserIdAndIsRead(Long userId, boolean isRead);
	
	// ── Mark as read ──────────────────────────────────────────────────────────
	@Modifying
	@Query("UPDATE Notification n SET n.isRead = true, n.readAt = CURRENT_TIMESTAMP WHERE n.id = :id")
	void markAsRead(@Param("id") Long id);
	
	// ── Mark all as read for a user ───────────────────────────────────────────
	@Modifying
	@Query("UPDATE Notification n SET n.isRead = true, n.readAt = CURRENT_TIMESTAMP WHERE n.userId = :userId AND n" +
		       ".isRead = false")
	void markAllAsRead(@Param("userId") Long userId);
	
	// ── By type ───────────────────────────────────────────────────────────────
	List<Notification> findByUserIdAndType(Long userId, NotificationType type);
	
}
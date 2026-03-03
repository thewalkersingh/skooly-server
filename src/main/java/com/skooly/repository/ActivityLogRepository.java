package com.skooly.repository;
import com.skooly.model.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
	Page<ActivityLog> findByUserId(Long userId, Pageable pageable);
	
	Page<ActivityLog> findByModule(String module, Pageable pageable);
	
	Page<ActivityLog> findByAction(ActivityLog.Action action, Pageable pageable);
	
	@Query("""
			    SELECT a FROM ActivityLog a
			    WHERE (:userId IS NULL OR a.user.id = :userId)
			    AND (:module IS NULL OR a.module = :module)
			    AND (:action IS NULL OR a.action = :action)
			    AND (:from IS NULL OR a.createdAt >= :from)
			    AND (:to IS NULL OR a.createdAt <= :to)
			    ORDER BY a.createdAt DESC
			""")
	Page<ActivityLog> findWithFilters(
			@Param("userId") Long userId,
			@Param("module") String module,
			@Param("action") ActivityLog.Action action,
			@Param("from") LocalDateTime from,
			@Param("to") LocalDateTime to,
			Pageable pageable
	                                 );
	
	@Modifying
	@Query("DELETE FROM ActivityLog a WHERE a.createdAt < :before")
	void deleteLogsOlderThan(@Param("before") LocalDateTime before);
}
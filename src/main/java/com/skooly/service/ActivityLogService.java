package com.skooly.service;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.response.ActivityLogResponse;
import com.skooly.model.ActivityLog;

import java.time.LocalDateTime;

public interface ActivityLogService {
	PageResponse<ActivityLogResponse> getAllLogs(
			int page, int size,
			Long userId,
			String module,
			ActivityLog.Action action,
			LocalDateTime from,
			LocalDateTime to
	                                            );
	
	ActivityLogResponse getLogById(Long id);
	
	void deleteLog(Long id);
	
	void clearAllLogs();
	
	// Called internally by other services
	void log(Long userId, ActivityLog.Action action, String module,
			String description, String ipAddress);
}
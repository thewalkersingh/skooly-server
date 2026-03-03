package com.skooly.controller;
import com.skooly.dto.common.ApiResponse;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.response.ActivityLogResponse;
import com.skooly.model.ActivityLog;
import com.skooly.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/activity-logs")
@RequiredArgsConstructor
public class ActivityLogController {
	private final ActivityLogService activityLogService;
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<PageResponse<ActivityLogResponse>>> getAllLogs(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int size,
			@RequestParam(required = false) Long userId,
			@RequestParam(required = false) String module,
			@RequestParam(required = false) ActivityLog.Action action,
			@RequestParam(required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
			@RequestParam(required = false)
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Activity logs fetched successfully",
		                                           activityLogService.getAllLogs(page, size, userId, module, action,
		                                                                         from,
		                                                                         to)));
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<ActivityLogResponse>> getLogById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Activity log fetched successfully",
		                                           activityLogService.getLogById(id)));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteLog(@PathVariable Long id) {
		activityLogService.deleteLog(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Activity log deleted successfully", null));
	}
	
	@DeleteMapping("/clear")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> clearAllLogs() {
		activityLogService.clearAllLogs();
		return ResponseEntity.ok(new ApiResponse<>(true, "All activity logs cleared", null));
	}
}
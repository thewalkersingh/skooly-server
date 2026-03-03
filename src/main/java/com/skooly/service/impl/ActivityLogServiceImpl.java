package com.skooly.service.impl;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.response.ActivityLogResponse;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.mapper.ActivityLogMapper;
import com.skooly.model.ActivityLog;
import com.skooly.repository.ActivityLogRepository;
import com.skooly.repository.UserRepository;
import com.skooly.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ActivityLogServiceImpl implements ActivityLogService {
	private final ActivityLogRepository activityLogRepository;
	private final UserRepository userRepository;
	private final ActivityLogMapper activityLogMapper;
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<ActivityLogResponse> getAllLogs(
			int page, int size,
			Long userId, String module,
			ActivityLog.Action action,
			LocalDateTime from, LocalDateTime to) {
		
		Pageable pageable = PageRequest.of(page-1, size, Sort.by("createdAt").descending());
		Page<ActivityLog> logs = activityLogRepository.findWithFilters(
				userId, module, action, from, to, pageable
		                                                              );
		List<ActivityLogResponse> data = logs.getContent()
				                                 .stream().map(activityLogMapper::toResponse).toList();
		return new PageResponse<>(data, page, size, logs.getTotalElements(), logs.getTotalPages());
	}
	
	@Override
	@Transactional(readOnly = true)
	public ActivityLogResponse getLogById(Long id) {
		ActivityLog log = activityLogRepository.findById(id)
				                  .orElseThrow(() -> new ResourceNotFoundException("Activity log not found with id: "+id));
		return activityLogMapper.toResponse(log);
	}
	
	@Override
	public void deleteLog(Long id) {
		if(!activityLogRepository.existsById(id)){
			throw new ResourceNotFoundException("Activity log not found with id: "+id);
		}
		activityLogRepository.deleteById(id);
	}
	
	@Override
	public void clearAllLogs() {
		activityLogRepository.deleteAll();
	}
	
	@Override
	@Async
	public void log(Long userId, ActivityLog.Action action,
			String module, String description, String ipAddress) {
		try{
			ActivityLog activityLog = ActivityLog.builder()
					                          .action(action)
					                          .module(module)
					                          .description(description)
					                          .ipAddress(ipAddress)
					                          .build();
			
			if(userId != null){
				userRepository.findById(userId).ifPresent(activityLog::setUser);
			}
			
			activityLogRepository.save(activityLog);
		} catch(Exception e){
			log.error("Failed to save activity log: {}", e.getMessage());
		}
	}
}
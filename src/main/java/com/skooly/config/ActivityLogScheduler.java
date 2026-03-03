package com.skooly.config;
import com.skooly.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivityLogScheduler {
	private final ActivityLogRepository activityLogRepository;
	
	// Runs at midnight on the 1st of every month
	@Scheduled(cron = "0 0 0 1 * *")
	@Transactional
	public void archiveOldLogs() {
		LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);
		activityLogRepository.deleteLogsOlderThan(sixMonthsAgo);
		log.info("Archived activity logs older than {}", sixMonthsAgo);
	}
}
package com.skooly.repository;
import com.skooly.model.MaintenanceLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceLogRepository extends JpaRepository<MaintenanceLog, Long> {
	Page<MaintenanceLog> findByFacilityId(Long facilityId, Pageable pageable);
	
	Page<MaintenanceLog> findByStatus(MaintenanceLog.MaintenanceStatus status, Pageable pageable);
}
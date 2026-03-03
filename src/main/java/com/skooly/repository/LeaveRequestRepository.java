package com.skooly.repository;
import com.skooly.model.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
	Page<LeaveRequest> findByStaffId(Long staffId, Pageable pageable);
	
	Page<LeaveRequest> findByStatus(LeaveRequest.LeaveStatus status, Pageable pageable);
	
	Page<LeaveRequest> findByStaffIdAndStatus(Long staffId, LeaveRequest.LeaveStatus status, Pageable pageable);
}
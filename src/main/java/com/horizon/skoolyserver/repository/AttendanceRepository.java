package com.horizon.skoolyserver.repository;
import com.horizon.skoolyserver.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	List<Attendance> findByStudentId(Long studentId);
	
	List<Attendance> findByDate(LocalDate date);
}
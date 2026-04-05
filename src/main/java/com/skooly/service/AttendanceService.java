package com.skooly.service;
import com.skooly.dto.request.AttendanceRequest;
import com.skooly.dto.response.AttendanceResponse;
import com.skooly.dto.response.AttendanceSummaryResponse;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
	List<AttendanceResponse> markAttendance(Long schoolId, AttendanceRequest req);
	
	List<AttendanceResponse> getByClassAndDate(Long schoolId, Long classId, LocalDate date);
	
	List<AttendanceResponse> getByStudent(Long schoolId, Long studentId);
	
	AttendanceSummaryResponse getTodaySummary(Long schoolId);
	
	List<AttendanceResponse> getByClassAndDateRange(Long schoolId, Long classId, LocalDate from, LocalDate to);
	
}
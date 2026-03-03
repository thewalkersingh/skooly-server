package com.skooly.service;
import com.skooly.dto.request.BulkAttendanceRequest;
import com.skooly.dto.request.MarkAttendanceRequest;
import com.skooly.dto.request.MarkTeacherAttendanceRequest;
import com.skooly.dto.response.AttendanceResponse;
import com.skooly.dto.response.AttendanceSummaryResponse;
import com.skooly.dto.response.TeacherAttendanceResponse;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
	// Student attendance
	AttendanceResponse markAttendance(MarkAttendanceRequest request, Long markedByUserId);
	
	List<AttendanceResponse> markBulkAttendance(BulkAttendanceRequest request, Long markedByUserId);
	
	AttendanceResponse updateAttendance(Long id, MarkAttendanceRequest request);
	
	void deleteAttendance(Long id);
	
	AttendanceResponse getAttendanceById(Long id);
	
	List<AttendanceResponse> getClassAttendanceByDate(Long classId, LocalDate date);
	
	List<AttendanceResponse> getStudentMonthlyAttendance(Long studentId, int month, int year);
	
	AttendanceSummaryResponse getStudentAttendanceSummary(Long studentId, LocalDate from, LocalDate to);
	
	List<AttendanceSummaryResponse> getLowAttendanceStudents(Long classId, int month, int year, double threshold);
	
	// Teacher attendance
	TeacherAttendanceResponse markTeacherAttendance(MarkTeacherAttendanceRequest request);
	
	List<TeacherAttendanceResponse> markBulkTeacherAttendance(List<MarkTeacherAttendanceRequest> requests);
	
	TeacherAttendanceResponse updateTeacherAttendance(Long id, MarkTeacherAttendanceRequest request);
	
	void deleteTeacherAttendance(Long id);
	
	List<TeacherAttendanceResponse> getTeacherMonthlyAttendance(Long teacherId, int month, int year);
}
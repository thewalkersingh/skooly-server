package com.skooly.service.impl;
import com.skooly.dto.request.AttendanceRequest;
import com.skooly.dto.response.AttendanceResponse;
import com.skooly.dto.response.AttendanceSummaryResponse;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.model.*;
import com.skooly.repository.*;
import com.skooly.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
	private final AttendanceRepository attendanceRepository;
	private final SchoolRepository schoolRepository;
	private final SchoolClassRepository classRepository;
	private final StudentRepository studentRepository;
	
	// ── Mark attendance (bulk for a class on a date) ──────
	@Transactional
	public List<AttendanceResponse> markAttendance(Long schoolId, AttendanceRequest req) {
		School school = schoolRepository.findById(schoolId)
				                .orElseThrow(() -> new ResourceNotFoundException("School", schoolId));
		
		SchoolClass schoolClass = classRepository.findById(req.getClassId())
				                          .orElseThrow(() -> new ResourceNotFoundException("Class", req.getClassId()));
		
		for(AttendanceRequest.AttendanceEntry entry : req.getRecords()){
			Student student = studentRepository.findByIdAndSchoolId(entry.getStudentId(), schoolId)
					                  .orElseThrow(() -> new ResourceNotFoundException("Student", entry.getStudentId()));
			
			// Upsert — update if already marked, else create
			Attendance attendance = attendanceRepository
					                        .findBySchoolIdAndStudentIdAndDate(
							                        schoolId, entry.getStudentId(), req.getDate())
					                        .orElse(Attendance.builder()
							                                .school(school)
							                                .student(student)
							                                .schoolClass(schoolClass)
							                                .date(req.getDate())
							                                .build());
			
			attendance.setStatus(Attendance.Status.valueOf(entry.getStatus()));
			attendance.setRemarks(entry.getRemarks());
			attendanceRepository.save(attendance);
		}
		
		return attendanceRepository
				       .findBySchoolIdAndSchoolClassIdAndDate(schoolId, req.getClassId(), req.getDate())
				       .stream().map(AttendanceResponse::from).toList();
	}
	
	// ── Get attendance by class and date ──────────────────
	public List<AttendanceResponse> getByClassAndDate(Long schoolId, Long classId, LocalDate date) {
		return attendanceRepository
				       .findBySchoolIdAndSchoolClassIdAndDate(schoolId, classId, date)
				       .stream().map(AttendanceResponse::from).toList();
	}
	
	// ── Get attendance history for a student ──────────────
	public List<AttendanceResponse> getByStudent(Long schoolId, Long studentId) {
		return attendanceRepository
				       .findBySchoolIdAndStudentId(schoolId, studentId)
				       .stream().map(AttendanceResponse::from).toList();
	}
	
	// ── Get today's summary ───────────────────────────────
	public AttendanceSummaryResponse getTodaySummary(Long schoolId) {
		LocalDate today = LocalDate.now();
		long total = attendanceRepository.countBySchoolIdAndDate(schoolId, today);
		long present = attendanceRepository.countBySchoolIdAndDateAndStatus(schoolId, today, Attendance.Status.PRESENT);
		long absent = attendanceRepository.countBySchoolIdAndDateAndStatus(schoolId, today, Attendance.Status.ABSENT);
		long late = attendanceRepository.countBySchoolIdAndDateAndStatus(schoolId, today, Attendance.Status.LATE);
		double pct = total > 0 ? Math.round((present * 100.0 / total) * 10.0) / 10.0 : 0;
		return new AttendanceSummaryResponse(total, present, absent, late, pct);
	}
	
	// ── Get attendance for a class between dates ──────────
	public List<AttendanceResponse> getByClassAndDateRange(Long schoolId, Long classId, LocalDate from, LocalDate to) {
		return
				attendanceRepository
						.findBySchoolIdAndSchoolClassIdAndDateBetween(schoolId, classId, from, to)
						.stream().map(AttendanceResponse::from).toList();
	}
	
}
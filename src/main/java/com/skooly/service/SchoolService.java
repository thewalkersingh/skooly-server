package com.skooly.service;

import com.skooly.dto.request.SchoolRequest;
import com.skooly.dto.response.SchoolResponse;
import com.skooly.enums.SchoolStatus;
import com.skooly.wrapper.PageResponse;
import org.springframework.data.domain.Pageable;

public interface SchoolService {
	
	// ── Create / Update / Delete ──────────────────────────────────────────────
	SchoolResponse createSchool(SchoolRequest schoolRequest);
	
	SchoolResponse updateSchool(Long schoolId, SchoolRequest schoolRequest);
	
	void deleteSchool(Long schoolId);
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	SchoolResponse getSchool(Long schoolId);
	
	SchoolResponse getSchoolByCode(String schoolCode);
	
	SchoolResponse getSchoolByEmail(String email);
	
	SchoolResponse getSchoolByPhone(String phone);
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	PageResponse<SchoolResponse> getPublicSchools(Pageable pageable);
	
	PageResponse<SchoolResponse> getAllSchools(Pageable pageable);
	
	PageResponse<SchoolResponse> getSchoolsBySchoolStatus(SchoolStatus schoolStatus, Pageable pageable);
	
	PageResponse<SchoolResponse> searchSchoolsByName(String name, Pageable pageable);
	
	// ── Status management ─────────────────────────────────────────────────────
	SchoolResponse updateStatus(Long schoolId, SchoolStatus schoolStatus);
	
	// ── Existence checks (useful before creating classrooms/teachers) ─────────
	boolean existsByCode(String schoolCode);
	
	boolean existsByEmail(String email);
	
}
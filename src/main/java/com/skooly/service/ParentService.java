package com.skooly.service;

import com.skooly.dto.common.StudentSummary;
import com.skooly.dto.request.ParentRequest;
import com.skooly.dto.response.ParentResponse;
import com.skooly.enums.ParentStatus;
import com.skooly.wrapper.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ParentService {
	
	// ── Create / Update / Delete ──────────────────────────────────────────────
	ParentResponse createParent(ParentRequest request);
	
	ParentResponse updateParent(Long parentId, ParentRequest request);
	
	void deleteParent(Long parentId);
	
	// Status management — for soft delete
	ParentResponse updateStatus(Long parentId, ParentStatus status);
	
	// ── Single fetch ──────────────────────────────────────────────────────────
	ParentResponse getParent(Long parentId);
	
	ParentResponse getParentByPhone(String phone);
	
	ParentResponse getParentByEmail(String email);
	
	// Fetches parent with identity eagerly loaded
	ParentResponse getParentWithIdentity(Long parentId);
	
	// ── Lists ─────────────────────────────────────────────────────────────────
	PageResponse<ParentResponse> getAllParents(Pageable pageable);
	
	// Get all children of a parent
	List<StudentSummary> getStudentsByParent(Long parentId);
	
	// All parents who have at least one child in the given school
	PageResponse<ParentResponse> getParentsBySchool(Long schoolId, Pageable pageable);
	
	// Parents with more than one child enrolled in the school
	List<ParentResponse> getParentsWithMultipleChildren(Long schoolId);
	
	// ── Search ────────────────────────────────────────────────────────────────
	PageResponse<ParentResponse> searchParentsByName(String name, Pageable pageable);
	
}
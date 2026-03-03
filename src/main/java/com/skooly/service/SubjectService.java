package com.skooly.service;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateSubjectRequest;
import com.skooly.dto.response.SubjectResponse;

import java.util.List;

public interface SubjectService {
	PageResponse<SubjectResponse> getAllSubjects(int page, int size, String search);
	
	SubjectResponse getSubjectById(Long id);
	
	List<SubjectResponse> getSubjectsByClass(Long classId);
	
	SubjectResponse createSubject(CreateSubjectRequest request);
	
	SubjectResponse updateSubject(Long id, CreateSubjectRequest request);
	
	void deleteSubject(Long id);
}
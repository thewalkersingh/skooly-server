package com.skooly.service;
import com.skooly.dto.request.BookIssueRequest;
import com.skooly.dto.response.BookIssueResponse;

import java.util.List;

public interface BookIssueService {
	public List<BookIssueResponse> getAllIssues(Long schoolId);
	
	public List<BookIssueResponse> searchIssues(Long schoolId, String query);
	
	BookIssueResponse issueBook(Long schoolId, BookIssueRequest req);
	
	public BookIssueResponse returnBook(Long schoolId, Long issueId);
	
	void deleteIssue(Long schoolId, Long issueId);
	
}

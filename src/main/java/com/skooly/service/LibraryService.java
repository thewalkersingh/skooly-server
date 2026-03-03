package com.skooly.service;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateBookRequest;
import com.skooly.dto.request.IssueBookRequest;
import com.skooly.dto.response.BookIssueResponse;
import com.skooly.dto.response.BookResponse;
import com.skooly.model.BookIssue;

import java.util.List;

public interface LibraryService {
	// Books
	PageResponse<BookResponse> getAllBooks(int page, int size, String search,
			String author, String category, Boolean available);
	
	BookResponse getBookById(Long id);
	
	BookResponse createBook(CreateBookRequest request);
	
	BookResponse updateBook(Long id, CreateBookRequest request);
	
	void deleteBook(Long id);
	
	// Book Issues
	PageResponse<BookIssueResponse> getAllIssues(int page, int size, Long studentId,
			BookIssue.IssueStatus status);
	
	BookIssueResponse getIssueById(Long id);
	
	BookIssueResponse issueBook(IssueBookRequest request);
	
	BookIssueResponse returnBook(Long issueId);
	
	List<BookIssueResponse> getOverdueBooks();
	
	// Fines
	PageResponse<BookIssueResponse> getAllFines(int page, int size, Long studentId);
	
	BookIssueResponse payFine(Long issueId);
}
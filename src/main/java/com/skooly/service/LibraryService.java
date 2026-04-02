package com.skooly.service;
import com.skooly.dto.request.BookRequest;
import com.skooly.dto.response.BookResponse;

import java.util.List;

public interface LibraryService {
	List<BookResponse> getAllBooks(Long schoolId);
	
	List<BookResponse> searchBooks(Long schoolId, String query);
	
	BookResponse createBook(Long schoolId, BookRequest req);
	
	BookResponse updateBook(Long schoolId, Long bookId, BookRequest req);
	
	void deleteBook(Long schoolId, Long bookId);
	
}

package com.skooly.service.impl;
import com.skooly.dto.request.BookRequest;
import com.skooly.dto.response.BookResponse;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.model.Book;
import com.skooly.model.School;
import com.skooly.repository.*;
import com.skooly.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {
	private final BookRepository bookRepository;
	private final BookIssueRepository bookIssueRepository;
	private final SchoolRepository schoolRepository;
	private final StudentRepository studentRepository;
	
	// ── Books ─────────────────────────────────────────────
	
	public List<BookResponse> getAllBooks(Long schoolId) {
		return bookRepository.findBySchoolId(schoolId)
				       .stream().map(BookResponse::from).toList();
	}
	
	public List<BookResponse> searchBooks(Long schoolId, String query) {
		return bookRepository.search(schoolId, query)
				       .stream().map(BookResponse::from).toList();
	}
	
	@Transactional
	public BookResponse createBook(Long schoolId, BookRequest req) {
		School school = schoolRepository.findById(schoolId)
				                .orElseThrow(() -> new ResourceNotFoundException("School", schoolId));
		Book book = Book.builder()
//				            .school(school)
				            .title(req.getTitle())
				            .author(req.getAuthor())
				            .isbn(req.getIsbn())
				            .category(req.getCategory())
				            .publisher(req.getPublisher())
				            .publishedYear(req.getPublishedYear())
				            .totalCopies(req.getTotalCopies() != null ? req.getTotalCopies() : 1)
				            .availableCopies(req.getTotalCopies() != null ? req.getTotalCopies() : 1)
				            .build();
		return BookResponse.from(bookRepository.save(book));
	}
	
	@Transactional
	public BookResponse updateBook(Long schoolId, Long bookId, BookRequest req) {
		Book book = bookRepository.findByIdAndSchoolId(bookId, schoolId)
				            .orElseThrow(() -> new ResourceNotFoundException("Book", bookId));
		book.setTitle(req.getTitle());
		book.setAuthor(req.getAuthor());
		book.setIsbn(req.getIsbn());
		book.setCategory(req.getCategory());
		book.setPublisher(req.getPublisher());
		book.setPublishedYear(req.getPublishedYear());
		if(req.getTotalCopies() != null){
			int diff = req.getTotalCopies() - book.getTotalCopies();
			book.setTotalCopies(req.getTotalCopies());
			book.setAvailableCopies(Math.max(0, book.getAvailableCopies() + diff));
		}
		return BookResponse.from(bookRepository.save(book));
	}
	
	@Transactional
	public void deleteBook(Long schoolId, Long bookId) {
		Book book = bookRepository.findByIdAndSchoolId(bookId, schoolId)
				            .orElseThrow(() -> new ResourceNotFoundException("Book", bookId));
		bookRepository.delete(book);
	}
	
}

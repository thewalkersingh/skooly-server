package com.skooly.service.impl;
import com.skooly.dto.request.BookIssueRequest;
import com.skooly.dto.response.BookIssueResponse;
import com.skooly.exception.BadRequestException;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.model.*;
import com.skooly.repository.*;
import com.skooly.service.BookIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookIssueServiceImpl implements BookIssueService {
	private final BookRepository bookRepository;
	private final BookIssueRepository bookIssueRepository;
	private final SchoolRepository schoolRepository;
	private final StudentRepository studentRepository;
	
	public List<BookIssueResponse> getAllIssues(Long schoolId) {
		return bookIssueRepository.findBySchoolId(schoolId)
				       .stream().map(BookIssueResponse::from).toList();
	}
	
	public List<BookIssueResponse> searchIssues(Long schoolId, String query) {
		return bookIssueRepository.search(schoolId, query)
				       .stream().map(BookIssueResponse::from).toList();
	}
	
	@Transactional
	public BookIssueResponse issueBook(Long schoolId, BookIssueRequest req) {
		School school = schoolRepository.findById(schoolId)
				                .orElseThrow(() -> new ResourceNotFoundException("School", schoolId));
		
		Book book = bookRepository.findByIdAndSchoolId(req.getBookId(), schoolId)
				            .orElseThrow(() -> new ResourceNotFoundException("Book", req.getBookId()));
		
		if(book.getAvailableCopies() < 1){
			throw new BadRequestException("No copies available for this book");
		}
		
		Student student = studentRepository.findByIdAndSchoolId(req.getStudentId(), schoolId)
				                  .orElseThrow(() -> new ResourceNotFoundException("Student", req.getStudentId()));
		
		if(bookIssueRepository.existsByBookIdAndStudentIdAndStatus(
				req.getBookId(), req.getStudentId(), BookIssue.Status.ISSUED)){
			throw new BadRequestException("This student already has this book issued");
		}
		
		// Decrease available copies
		book.setAvailableCopies(book.getAvailableCopies() - 1);
		bookRepository.save(book);
		
		BookIssue issue = BookIssue.builder()
//				                  .school(school)
				                  .book(book)
				                  .student(student)
				                  .issueDate(req.getIssueDate())
				                  .dueDate(req.getDueDate())
				                  .status(BookIssue.Status.ISSUED)
				                  .build();
		
		return BookIssueResponse.from(bookIssueRepository.save(issue));
	}
	
	@Transactional
	public BookIssueResponse returnBook(Long schoolId, Long issueId) {
		BookIssue issue = bookIssueRepository.findByIdAndSchoolId(issueId, schoolId)
				                  .orElseThrow(() -> new ResourceNotFoundException("BookIssue", issueId));
		
		if(issue.getStatus() == BookIssue.Status.RETURNED){
			throw new BadRequestException("This book has already been returned");
		}
		
		// Increase available copies
		Book book = issue.getBook();
		book.setAvailableCopies(book.getAvailableCopies() + 1);
		bookRepository.save(book);
		
		issue.setReturnDate(LocalDate.now());
		issue.setStatus(BookIssue.Status.RETURNED);
		
		return BookIssueResponse.from(bookIssueRepository.save(issue));
	}
	
	@Transactional
	public void deleteIssue(Long schoolId, Long issueId) {
		BookIssue issue = bookIssueRepository.findByIdAndSchoolId(issueId, schoolId)
				                  .orElseThrow(() -> new ResourceNotFoundException("BookIssue", issueId));
		// If still issued, restore available copy
		if(issue.getStatus() == BookIssue.Status.ISSUED){
			issue.getBook().setAvailableCopies(issue.getBook().getAvailableCopies() + 1);
			bookRepository.save(issue.getBook());
		}
		bookIssueRepository.delete(issue);
	}
	
}

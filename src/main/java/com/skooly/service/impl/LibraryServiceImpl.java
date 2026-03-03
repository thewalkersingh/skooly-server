package com.skooly.service.impl;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateBookRequest;
import com.skooly.dto.request.IssueBookRequest;
import com.skooly.dto.response.BookIssueResponse;
import com.skooly.dto.response.BookResponse;
import com.skooly.exception.BadRequestException;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.mapper.LibraryMapper;
import com.skooly.model.Book;
import com.skooly.model.BookIssue;
import com.skooly.model.Student;
import com.skooly.repository.BookIssueRepository;
import com.skooly.repository.BookRepository;
import com.skooly.repository.StudentRepository;
import com.skooly.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LibraryServiceImpl implements LibraryService {
	private static final BigDecimal FINE_PER_DAY = new BigDecimal("2.00");
	private final BookRepository bookRepository;
	private final BookIssueRepository bookIssueRepository;
	private final StudentRepository studentRepository;
	private final LibraryMapper libraryMapper;
	
	// ── Books ────────────────────────────────────────────────────────────────
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<BookResponse> getAllBooks(
			int page, int size, String search,
			String author, String category, Boolean available) {
		
		Pageable pageable = PageRequest.of(page-1, size, Sort.by("title").ascending());
		Page<Book> books = bookRepository.findWithFilters(search, author, category, available, pageable);
		List<BookResponse> data = books.getContent().stream().map(libraryMapper::toBookResponse).toList();
		return new PageResponse<>(data, page, size, books.getTotalElements(), books.getTotalPages());
	}
	
	@Override
	@Transactional(readOnly = true)
	public BookResponse getBookById(Long id) {
		return libraryMapper.toBookResponse(findBookById(id));
	}
	
	@Override
	public BookResponse createBook(CreateBookRequest request) {
		if(request.getIsbn() != null && bookRepository.existsByIsbn(request.getIsbn())){
			throw new BadRequestException("Book with ISBN '"+request.getIsbn()+"' already exists");
		}
		Book book = libraryMapper.toBookEntity(request);
		book.setAvailableCopies(request.getTotalCopies() != null ? request.getTotalCopies() : 1);
		return libraryMapper.toBookResponse(bookRepository.save(book));
	}
	
	@Override
	public BookResponse updateBook(Long id, CreateBookRequest request) {
		Book book = findBookById(id);
		book.setTitle(request.getTitle());
		if(request.getAuthor() != null)
			book.setAuthor(request.getAuthor());
		if(request.getCategory() != null)
			book.setCategory(request.getCategory());
		if(request.getPublisher() != null)
			book.setPublisher(request.getPublisher());
		if(request.getPublishedYear() != null)
			book.setPublishedYear(request.getPublishedYear());
		if(request.getTotalCopies() != null){
			int diff = request.getTotalCopies()-book.getTotalCopies();
			book.setTotalCopies(request.getTotalCopies());
			book.setAvailableCopies(Math.max(0, book.getAvailableCopies()+diff));
		}
		return libraryMapper.toBookResponse(bookRepository.save(book));
	}
	
	@Override
	public void deleteBook(Long id) {
		if(!bookRepository.existsById(id)){
			throw new ResourceNotFoundException("Book not found with id: "+id);
		}
		bookRepository.deleteById(id);
	}
	
	// ── Book Issues ──────────────────────────────────────────────────────────
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<BookIssueResponse> getAllIssues(
			int page, int size, Long studentId, BookIssue.IssueStatus status) {
		
		Pageable pageable = PageRequest.of(page-1, size, Sort.by("issueDate").descending());
		Page<BookIssue> issues = studentId != null
		                         ? bookIssueRepository.findByStudentId(studentId, pageable)
		                         : status != null
		                           ? bookIssueRepository.findByStatus(status, pageable)
		                           : bookIssueRepository.findAll(pageable);
		
		List<BookIssueResponse> data = issues.getContent().stream().map(libraryMapper::toIssueResponse).toList();
		return new PageResponse<>(data, page, size, issues.getTotalElements(), issues.getTotalPages());
	}
	
	@Override
	@Transactional(readOnly = true)
	public BookIssueResponse getIssueById(Long id) {
		return libraryMapper.toIssueResponse(findIssueById(id));
	}
	
	@Override
	public BookIssueResponse issueBook(IssueBookRequest request) {
		Book book = findBookById(request.getBookId());
		
		if(book.getAvailableCopies() <= 0){
			throw new BadRequestException("No available copies for book: "+book.getTitle());
		}
		if(bookIssueRepository.existsByBookIdAndStudentIdAndStatus(
				request.getBookId(), request.getStudentId(), BookIssue.IssueStatus.ISSUED)){
			throw new BadRequestException("Student already has this book issued");
		}
		
		Student student = findStudentById(request.getStudentId());
		BookIssue issue = BookIssue.builder()
				                  .book(book).student(student)
				                  .issueDate(request.getIssueDate())
				                  .dueDate(request.getDueDate())
				                  .status(BookIssue.IssueStatus.ISSUED)
				                  .fine(BigDecimal.ZERO)
				                  .build();
		
		book.setAvailableCopies(book.getAvailableCopies()-1);
		bookRepository.save(book);
		
		return libraryMapper.toIssueResponse(bookIssueRepository.save(issue));
	}
	
	@Override
	public BookIssueResponse returnBook(Long issueId) {
		BookIssue issue = findIssueById(issueId);
		
		if(issue.getStatus() == BookIssue.IssueStatus.RETURNED){
			throw new BadRequestException("Book already returned");
		}
		
		LocalDate today = LocalDate.now();
		issue.setReturnDate(today);
		issue.setStatus(BookIssue.IssueStatus.RETURNED);
		
		if(today.isAfter(issue.getDueDate())){
			long overdueDays = ChronoUnit.DAYS.between(issue.getDueDate(), today);
			issue.setFine(FINE_PER_DAY.multiply(BigDecimal.valueOf(overdueDays)));
		}
		
		Book book = issue.getBook();
		book.setAvailableCopies(book.getAvailableCopies()+1);
		bookRepository.save(book);
		
		return libraryMapper.toIssueResponse(bookIssueRepository.save(issue));
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<BookIssueResponse> getOverdueBooks() {
		return bookIssueRepository.findOverdueBooks(LocalDate.now())
				       .stream().map(libraryMapper::toIssueResponse).toList();
	}
	
	// ── Fines ────────────────────────────────────────────────────────────────
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<BookIssueResponse> getAllFines(int page, int size, Long studentId) {
		Pageable pageable = PageRequest.of(page-1, size);
		Page<BookIssue> issues = studentId != null
		                         ? bookIssueRepository.findByStudentId(studentId, pageable)
		                         : bookIssueRepository.findAll(pageable);
		
		List<BookIssueResponse> data = issues.getContent().stream()
				                               .filter(i -> i.getFine().compareTo(BigDecimal.ZERO) > 0)
				                               .map(libraryMapper::toIssueResponse).toList();
		
		return new PageResponse<>(data, page, size, issues.getTotalElements(), issues.getTotalPages());
	}
	
	@Override
	public BookIssueResponse payFine(Long issueId) {
		BookIssue issue = findIssueById(issueId);
		issue.setFine(BigDecimal.ZERO);
		return libraryMapper.toIssueResponse(bookIssueRepository.save(issue));
	}
	
	// ── Private helpers ──────────────────────────────────────────────────────
	
	private Book findBookById(Long id) {
		return bookRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: "+id));
	}
	
	private BookIssue findIssueById(Long id) {
		return bookIssueRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Book issue not found with id: "+id));
	}
	
	private Student findStudentById(Long id) {
		return studentRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: "+id));
	}
}
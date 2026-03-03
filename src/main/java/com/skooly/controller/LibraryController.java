package com.skooly.controller;
import com.skooly.dto.common.ApiResponse;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.CreateBookRequest;
import com.skooly.dto.request.IssueBookRequest;
import com.skooly.dto.response.BookIssueResponse;
import com.skooly.dto.response.BookResponse;
import com.skooly.model.BookIssue;
import com.skooly.service.LibraryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/library")
@RequiredArgsConstructor
public class LibraryController {
	private final LibraryService libraryService;
	
	// ── Books ────────────────────────────────────────────────────────────────
	
	@GetMapping("/books")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
	public ResponseEntity<ApiResponse<PageResponse<BookResponse>>> getAllBooks(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String author,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) Boolean available) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Books fetched successfully",
		                                           libraryService.getAllBooks(page, size, search, author, category,
		                                                                      available)));
	}
	
	@GetMapping("/books/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER', 'STUDENT')")
	public ResponseEntity<ApiResponse<BookResponse>> getBookById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Book fetched successfully",
		                                           libraryService.getBookById(id)));
	}
	
	@PostMapping("/books")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<BookResponse>> createBook(
			@Valid @RequestBody CreateBookRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Book created successfully",
		                                                         libraryService.createBook(request)));
	}
	
	@PutMapping("/books/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<BookResponse>> updateBook(
			@PathVariable Long id, @Valid @RequestBody CreateBookRequest request) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Book updated successfully",
		                                           libraryService.updateBook(id, request)));
	}
	
	@DeleteMapping("/books/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long id) {
		libraryService.deleteBook(id);
		return ResponseEntity.ok(new ApiResponse<>(true, "Book deleted successfully", null));
	}
	
	// ── Book Issues ──────────────────────────────────────────────────────────
	
	@GetMapping("/issues")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<PageResponse<BookIssueResponse>>> getAllIssues(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) Long studentId,
			@RequestParam(required = false) BookIssue.IssueStatus status) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Issues fetched successfully",
		                                           libraryService.getAllIssues(page, size, studentId, status)));
	}
	
	@GetMapping("/issues/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
	public ResponseEntity<ApiResponse<BookIssueResponse>> getIssueById(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Issue fetched successfully",
		                                           libraryService.getIssueById(id)));
	}
	
	@PostMapping("/issues")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<BookIssueResponse>> issueBook(
			@Valid @RequestBody IssueBookRequest request) {
		return ResponseEntity.status(201).body(new ApiResponse<>(true, "Book issued successfully",
		                                                         libraryService.issueBook(request)));
	}
	
	@PatchMapping("/issues/{id}/return")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<BookIssueResponse>> returnBook(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Book returned successfully",
		                                           libraryService.returnBook(id)));
	}
	
	@GetMapping("/issues/overdue")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<List<BookIssueResponse>>> getOverdueBooks() {
		return ResponseEntity.ok(new ApiResponse<>(true, "Overdue books fetched successfully",
		                                           libraryService.getOverdueBooks()));
	}
	
	// ── Fines ────────────────────────────────────────────────────────────────
	
	@GetMapping("/fines")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<PageResponse<BookIssueResponse>>> getAllFines(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) Long studentId) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Fines fetched successfully",
		                                           libraryService.getAllFines(page, size, studentId)));
	}
	
	@PatchMapping("/fines/{id}/pay")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse<BookIssueResponse>> payFine(@PathVariable Long id) {
		return ResponseEntity.ok(new ApiResponse<>(true, "Fine paid successfully",
		                                           libraryService.payFine(id)));
	}
}
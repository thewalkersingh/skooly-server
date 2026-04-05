package com.skooly.controller;
import com.skooly.dto.request.BookIssueRequest;
import com.skooly.dto.request.BookRequest;
import com.skooly.dto.response.BookIssueResponse;
import com.skooly.dto.response.BookResponse;
import com.skooly.service.BookIssueService;
import com.skooly.service.LibraryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schools/{schoolId}/library")
@RequiredArgsConstructor
@Tag(name = "Library", description = "Library management endpoints")
public class LibraryController {
	private final LibraryService libraryService;
	private final BookIssueService bookIssueService;
	// ── Books ────────────────────────────────────────────
	
	@GetMapping("/books")
	public ResponseEntity<List<BookResponse>> getAllBooks(
			@PathVariable Long schoolId,
			@RequestParam(required = false) String search) {
		if(search != null && !search.isBlank())
			return ResponseEntity.ok(libraryService.searchBooks(schoolId, search));
		return ResponseEntity.ok(libraryService.getAllBooks(schoolId));
	}
	
	@PostMapping("/books")
	public ResponseEntity<BookResponse> createBook(
			@PathVariable Long schoolId,
			@Valid @RequestBody BookRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED)
				       .body(libraryService.createBook(schoolId, req));
	}
	
	@PutMapping("/books/{bookId}")
	public ResponseEntity<BookResponse> updateBook(
			@PathVariable Long schoolId,
			@PathVariable Long bookId,
			@Valid @RequestBody BookRequest req) {
		return ResponseEntity.ok(libraryService.updateBook(schoolId, bookId, req));
	}
	
	@DeleteMapping("/books/{bookId}")
	public ResponseEntity<Void> deleteBook(
			@PathVariable Long schoolId,
			@PathVariable Long bookId) {
		libraryService.deleteBook(schoolId, bookId);
		return ResponseEntity.noContent().build();
	}
	
	// ── Book Issues ───────────────────────────────────────
	
	@GetMapping("/issues")
	public ResponseEntity<List<BookIssueResponse>> getAllIssues(
			@PathVariable Long schoolId,
			@RequestParam(required = false) String search) {
		if(search != null && !search.isBlank())
			return ResponseEntity.ok(bookIssueService.searchIssues(schoolId, search));
		return ResponseEntity.ok(bookIssueService.getAllIssues(schoolId));
	}
	
	@PostMapping("/issues")
	public ResponseEntity<BookIssueResponse> issueBook(
			@PathVariable Long schoolId,
			@Valid @RequestBody BookIssueRequest req) {
		return ResponseEntity.status(HttpStatus.CREATED)
				       .body(bookIssueService.issueBook(schoolId, req));
	}
	
	@PatchMapping("/issues/{issueId}/return")
	public ResponseEntity<BookIssueResponse> returnBook(
			@PathVariable Long schoolId,
			@PathVariable Long issueId) {
		return ResponseEntity.ok(bookIssueService.returnBook(schoolId, issueId));
	}
	
	@DeleteMapping("/issues/{issueId}")
	public ResponseEntity<Void> deleteIssue(
			@PathVariable Long schoolId,
			@PathVariable Long issueId) {
		bookIssueService.deleteIssue(schoolId, issueId);
		return ResponseEntity.noContent().build();
	}
	
}
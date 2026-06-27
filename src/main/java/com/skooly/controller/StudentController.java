//package com.skooly.controller;
//
//import com.skooly.service.StudentService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/students")
//@RequiredArgsConstructor
//public class StudentController {
//
//	private final StudentService studentService;
//
/// *	@PostMapping
//	public ApiResponse<StudentResponse> createStudent(@RequestBody StudentRequest request) {
//		StudentResponse response = studentService.createStudent(request);
//		return ApiResponse.<StudentResponse>builder()
//			        .success(true)
//			        .message("Student created successfully")
//			        .data(response)
//			        .build();
//	}*/
//
//	/*@GetMapping("/{id}")
//	public ApiResponse<StudentResponse> getStudent(@PathVariable Long id) {
//		StudentResponse response = studentService.getStudent(id);
//		return ApiResponse.<StudentResponse>builder()
//			        .success(true)
//			        .message("Student fetched successfully")
//			        .data(response)
//			        .build();
//	}
//
//	@GetMapping
//	public ApiResponse<PageResponse<StudentResponse>> getAllStudents(Pageable pageable) {
//		PageResponse<StudentResponse> response = studentService.getAllStudents(pageable);
//		return ApiResponse.<PageResponse<StudentResponse>>builder()
//			        .success(true)
//			        .message("Students fetched successfully")
//			        .data(response)
//			        .build();
//	}*/
//
//}
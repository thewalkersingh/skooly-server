package com.skooly.service;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.*;
import com.skooly.dto.response.*;
import com.skooly.model.LeaveRequest;
import com.skooly.model.Staff;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StaffService {
	// Departments
	List<DepartmentResponse> getAllDepartments();
	
	DepartmentResponse getDepartmentById(Long id);
	
	DepartmentResponse createDepartment(CreateDepartmentRequest request);
	
	DepartmentResponse updateDepartment(Long id, CreateDepartmentRequest request);
	
	void deleteDepartment(Long id);
	
	// Staff
	PageResponse<StaffSummaryResponse> getAllStaff(int page, int size, String search,
			Long departmentId, Staff.StaffStatus status,
			Staff.Gender gender);
	
	StaffResponse getStaffById(Long id);
	
	StaffResponse getMyProfile(Long userId);
	
	StaffResponse createStaff(CreateStaffRequest request);
	
	StaffResponse updateStaff(Long id, UpdateStaffRequest request);
	
	void deleteStaff(Long id);
	
	void updateStatus(Long id, Staff.StaffStatus status);
	
	StaffResponse uploadPhoto(Long id, MultipartFile file);
	
	void deletePhoto(Long id);
	
	// Leave Requests
	PageResponse<LeaveRequestResponse> getAllLeaveRequests(int page, int size,
			Long staffId,
			LeaveRequest.LeaveStatus status);
	
	LeaveRequestResponse getLeaveRequestById(Long id);
	
	LeaveRequestResponse createLeaveRequest(CreateLeaveRequest request);
	
	LeaveRequestResponse approveLeaveRequest(Long id, Long approvedByUserId);
	
	LeaveRequestResponse rejectLeaveRequest(Long id, Long approvedByUserId);
	
	void deleteLeaveRequest(Long id);
	
	// Payroll
	PageResponse<PayrollResponse> getAllPayrolls(int page, int size, Integer month, Integer year);
	
	List<PayrollResponse> getPayrollByStaff(Long staffId);
	
	PayrollResponse createPayroll(CreatePayrollRequest request);
	
	PayrollResponse updatePayroll(Long id, CreatePayrollRequest request);
	
	void deletePayroll(Long id);
}
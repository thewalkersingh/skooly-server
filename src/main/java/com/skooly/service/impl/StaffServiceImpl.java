package com.skooly.service.impl;
import com.skooly.dto.common.PageResponse;
import com.skooly.dto.request.*;
import com.skooly.dto.response.*;
import com.skooly.exception.BadRequestException;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.mapper.StaffMapper;
import com.skooly.model.*;
import com.skooly.repository.*;
import com.skooly.service.StaffService;
import com.skooly.utils.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffServiceImpl implements StaffService {
	private final StaffRepository staffRepository;
	private final DepartmentRepository departmentRepository;
	private final LeaveRequestRepository leaveRepository;
	private final PayrollRepository payrollRepository;
	private final UserRepository userRepository;
	private final StaffMapper staffMapper;
	private final FileUploadUtils fileUploadUtils;
	
	// ── Departments ──────────────────────────────────────────────────────────
	
	@Override
	@Transactional(readOnly = true)
	public List<DepartmentResponse> getAllDepartments() {
		return departmentRepository.findAll().stream().map(staffMapper::toDepartmentResponse).toList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public DepartmentResponse getDepartmentById(Long id) {
		return staffMapper.toDepartmentResponse(findDepartmentById(id));
	}
	
	@Override
	public DepartmentResponse createDepartment(CreateDepartmentRequest request) {
		if(departmentRepository.existsByName(request.getName())){
			throw new BadRequestException("Department '"+request.getName()+"' already exists");
		}
		Department dept = Department.builder()
				                  .name(request.getName())
				                  .description(request.getDescription())
				                  .build();
		if(request.getHeadId() != null){
			dept.setHead(findStaffById(request.getHeadId()));
		}
		return staffMapper.toDepartmentResponse(departmentRepository.save(dept));
	}
	
	@Override
	public DepartmentResponse updateDepartment(Long id, CreateDepartmentRequest request) {
		Department dept = findDepartmentById(id);
		dept.setName(request.getName());
		if(request.getDescription() != null)
			dept.setDescription(request.getDescription());
		if(request.getHeadId() != null)
			dept.setHead(findStaffById(request.getHeadId()));
		return staffMapper.toDepartmentResponse(departmentRepository.save(dept));
	}
	
	@Override
	public void deleteDepartment(Long id) {
		if(!departmentRepository.existsById(id)){
			throw new ResourceNotFoundException("Department not found with id: "+id);
		}
		departmentRepository.deleteById(id);
	}
	
	// ── Staff ────────────────────────────────────────────────────────────────
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<StaffSummaryResponse> getAllStaff(
			int page, int size, String search,
			Long departmentId, Staff.StaffStatus status, Staff.Gender gender) {
		
		Pageable pageable = PageRequest.of(page-1, size, Sort.by("firstName").ascending());
		Page<Staff> staff = staffRepository.findWithFilters(departmentId, status, gender, search, pageable);
		List<StaffSummaryResponse> data = staff.getContent().stream().map(staffMapper::toSummaryResponse).toList();
		return new PageResponse<>(data, page, size, staff.getTotalElements(), staff.getTotalPages());
	}
	
	@Override
	@Transactional(readOnly = true)
	public StaffResponse getStaffById(Long id) {
		return staffMapper.toResponse(findStaffById(id));
	}
	
	@Override
	@Transactional(readOnly = true)
	public StaffResponse getMyProfile(Long userId) {
		Staff staff = staffRepository.findByUserId(userId)
				              .orElseThrow(() -> new ResourceNotFoundException("Staff profile not found"));
		return staffMapper.toResponse(staff);
	}
	
	@Override
	public StaffResponse createStaff(CreateStaffRequest request) {
		if(request.getEmail() != null && staffRepository.existsByEmail(request.getEmail())){
			throw new BadRequestException("Email already in use");
		}
		User user = userRepository.findById(request.getUserId())
				            .orElseThrow(
						            () -> new ResourceNotFoundException("User not found with id: "+request.getUserId()));
		
		Staff staff = staffMapper.toEntity(request);
		staff.setUser(user);
		staff.setStatus(Staff.StaffStatus.ACTIVE);
		
		if(request.getDepartmentId() != null){
			staff.setDepartment(findDepartmentById(request.getDepartmentId()));
		}
		return staffMapper.toResponse(staffRepository.save(staff));
	}
	
	@Override
	public StaffResponse updateStaff(Long id, UpdateStaffRequest request) {
		Staff staff = findStaffById(id);
		applyUpdates(staff, request);
		return staffMapper.toResponse(staffRepository.save(staff));
	}
	
	@Override
	public void deleteStaff(Long id) {
		if(!staffRepository.existsById(id)){
			throw new ResourceNotFoundException("Staff not found with id: "+id);
		}
		staffRepository.deleteById(id);
	}
	
	@Override
	public void updateStatus(Long id, Staff.StaffStatus status) {
		Staff staff = findStaffById(id);
		staff.setStatus(status);
		staffRepository.save(staff);
	}
	
	@Override
	public StaffResponse uploadPhoto(Long id, MultipartFile file) {
		Staff staff = findStaffById(id);
		if(staff.getPhoto() != null)
			fileUploadUtils.deleteFile(staff.getPhoto());
		staff.setPhoto(fileUploadUtils.uploadFile(file, "staff"));
		return staffMapper.toResponse(staffRepository.save(staff));
	}
	
	@Override
	public void deletePhoto(Long id) {
		Staff staff = findStaffById(id);
		if(staff.getPhoto() != null){
			fileUploadUtils.deleteFile(staff.getPhoto());
			staff.setPhoto(null);
			staffRepository.save(staff);
		}
	}
	
	// ── Leave Requests ───────────────────────────────────────────────────────
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<LeaveRequestResponse> getAllLeaveRequests(
			int page, int size, Long staffId, LeaveRequest.LeaveStatus status) {
		
		Pageable pageable = PageRequest.of(page-1, size, Sort.by("createdAt").descending());
		Page<LeaveRequest> leaves;
		
		if(staffId != null && status != null){
			leaves = leaveRepository.findByStaffIdAndStatus(staffId, status, pageable);
		} else if(staffId != null){
			leaves = leaveRepository.findByStaffId(staffId, pageable);
		} else if(status != null){
			leaves = leaveRepository.findByStatus(status, pageable);
		} else{
			leaves = leaveRepository.findAll(pageable);
		}
		
		List<LeaveRequestResponse> data = leaves.getContent().stream().map(staffMapper::toLeaveResponse).toList();
		return new PageResponse<>(data, page, size, leaves.getTotalElements(), leaves.getTotalPages());
	}
	
	@Override
	@Transactional(readOnly = true)
	public LeaveRequestResponse getLeaveRequestById(Long id) {
		return staffMapper.toLeaveResponse(findLeaveById(id));
	}
	
	@Override
	public LeaveRequestResponse createLeaveRequest(CreateLeaveRequest request) {
		if(request.getFromDate().isAfter(request.getToDate())){
			throw new BadRequestException("From date cannot be after to date");
		}
		Staff staff = findStaffById(request.getStaffId());
		LeaveRequest leave = LeaveRequest.builder()
				                     .staff(staff)
				                     .leaveType(request.getLeaveType())
				                     .fromDate(request.getFromDate())
				                     .toDate(request.getToDate())
				                     .reason(request.getReason())
				                     .status(LeaveRequest.LeaveStatus.PENDING)
				                     .build();
		return staffMapper.toLeaveResponse(leaveRepository.save(leave));
	}
	
	@Override
	public LeaveRequestResponse approveLeaveRequest(Long id, Long approvedByUserId) {
		LeaveRequest leave = findLeaveById(id);
		if(leave.getStatus() != LeaveRequest.LeaveStatus.PENDING){
			throw new BadRequestException("Only pending leave requests can be approved");
		}
		User approvedBy = userRepository.findById(approvedByUserId)
				                  .orElseThrow(() -> new ResourceNotFoundException("User not found"));
		leave.setStatus(LeaveRequest.LeaveStatus.APPROVED);
		leave.setApprovedBy(approvedBy);
		return staffMapper.toLeaveResponse(leaveRepository.save(leave));
	}
	
	@Override
	public LeaveRequestResponse rejectLeaveRequest(Long id, Long approvedByUserId) {
		LeaveRequest leave = findLeaveById(id);
		if(leave.getStatus() != LeaveRequest.LeaveStatus.PENDING){
			throw new BadRequestException("Only pending leave requests can be rejected");
		}
		User approvedBy = userRepository.findById(approvedByUserId)
				                  .orElseThrow(() -> new ResourceNotFoundException("User not found"));
		leave.setStatus(LeaveRequest.LeaveStatus.REJECTED);
		leave.setApprovedBy(approvedBy);
		return staffMapper.toLeaveResponse(leaveRepository.save(leave));
	}
	
	@Override
	public void deleteLeaveRequest(Long id) {
		if(!leaveRepository.existsById(id)){
			throw new ResourceNotFoundException("Leave request not found with id: "+id);
		}
		leaveRepository.deleteById(id);
	}
	
	// ── Payroll ──────────────────────────────────────────────────────────────
	
	@Override
	@Transactional(readOnly = true)
	public PageResponse<PayrollResponse> getAllPayrolls(int page, int size, Integer month, Integer year) {
		Pageable pageable = PageRequest.of(page-1, size,
		                                   Sort.by("year").descending().and(Sort.by("month").descending()));
		Page<Payroll> payrolls = month != null && year != null
		                         ? payrollRepository.findByMonthAndYear(month, year, pageable)
		                         : payrollRepository.findAll(pageable);
		List<PayrollResponse> data = payrolls.getContent().stream().map(staffMapper::toPayrollResponse).toList();
		return new PageResponse<>(data, page, size, payrolls.getTotalElements(), payrolls.getTotalPages());
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<PayrollResponse> getPayrollByStaff(Long staffId) {
		return payrollRepository.findByStaffId(staffId).stream().map(staffMapper::toPayrollResponse).toList();
	}
	
	@Override
	public PayrollResponse createPayroll(CreatePayrollRequest request) {
		if(payrollRepository.existsByStaffIdAndMonthAndYear(
				request.getStaffId(), request.getMonth(), request.getYear())){
			throw new BadRequestException("Payroll already exists for this staff for "+
			                              request.getMonth()+"/"+request.getYear());
		}
		Staff staff = findStaffById(request.getStaffId());
		BigDecimal allowances = request.getAllowances() != null ? request.getAllowances() : BigDecimal.ZERO;
		BigDecimal deductions = request.getDeductions() != null ? request.getDeductions() : BigDecimal.ZERO;
		BigDecimal netSalary = request.getBasicSalary().add(allowances).subtract(deductions);
		
		Payroll payroll = Payroll.builder()
				                  .staff(staff)
				                  .month(request.getMonth())
				                  .year(request.getYear())
				                  .basicSalary(request.getBasicSalary())
				                  .allowances(allowances)
				                  .deductions(deductions)
				                  .netSalary(netSalary)
				                  .paidDate(request.getPaidDate())
				                  .build();
		return staffMapper.toPayrollResponse(payrollRepository.save(payroll));
	}
	
	@Override
	public PayrollResponse updatePayroll(Long id, CreatePayrollRequest request) {
		Payroll payroll = payrollRepository.findById(id)
				                  .orElseThrow(() -> new ResourceNotFoundException("Payroll not found with id: "+id));
		BigDecimal allowances = request.getAllowances() != null ? request.getAllowances() : payroll.getAllowances();
		BigDecimal deductions = request.getDeductions() != null ? request.getDeductions() : payroll.getDeductions();
		BigDecimal basic = request.getBasicSalary() != null ? request.getBasicSalary() : payroll.getBasicSalary();
		payroll.setBasicSalary(basic);
		payroll.setAllowances(allowances);
		payroll.setDeductions(deductions);
		payroll.setNetSalary(basic.add(allowances).subtract(deductions));
		if(request.getPaidDate() != null)
			payroll.setPaidDate(request.getPaidDate());
		return staffMapper.toPayrollResponse(payrollRepository.save(payroll));
	}
	
	@Override
	public void deletePayroll(Long id) {
		if(!payrollRepository.existsById(id)){
			throw new ResourceNotFoundException("Payroll not found with id: "+id);
		}
		payrollRepository.deleteById(id);
	}
	
	// ── Private helpers ──────────────────────────────────────────────────────
	
	private Staff findStaffById(Long id) {
		return staffRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Staff not found with id: "+id));
	}
	
	private Department findDepartmentById(Long id) {
		return departmentRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: "+id));
	}
	
	private LeaveRequest findLeaveById(Long id) {
		return leaveRepository.findById(id)
				       .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: "+id));
	}
	
	private void applyUpdates(Staff staff, UpdateStaffRequest request) {
		if(request.getFirstName() != null)
			staff.setFirstName(request.getFirstName());
		if(request.getLastName() != null)
			staff.setLastName(request.getLastName());
		if(request.getDob() != null)
			staff.setDob(request.getDob());
		if(request.getGender() != null)
			staff.setGender(request.getGender());
		if(request.getAddress() != null)
			staff.setAddress(request.getAddress());
		if(request.getPhone() != null)
			staff.setPhone(request.getPhone());
		if(request.getEmail() != null)
			staff.setEmail(request.getEmail());
		if(request.getDesignation() != null)
			staff.setDesignation(request.getDesignation());
		if(request.getJoiningDate() != null)
			staff.setJoiningDate(request.getJoiningDate());
		if(request.getSalary() != null)
			staff.setSalary(request.getSalary());
		if(request.getDepartmentId() != null)
			staff.setDepartment(findDepartmentById(request.getDepartmentId()));
	}
}
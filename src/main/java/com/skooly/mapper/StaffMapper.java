package com.skooly.mapper;
import com.skooly.dto.request.CreateStaffRequest;
import com.skooly.dto.response.*;
import com.skooly.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StaffMapper {
	@Mapping(target = "userId", source = "user.id")
	@Mapping(target = "departmentId", source = "department.id")
	@Mapping(target = "departmentName", source = "department.name")
	@Mapping(target = "gender", expression = "java(s.getGender() != null ? s.getGender().name() : null)")
	@Mapping(target = "status", expression = "java(s.getStatus() != null ? s.getStatus().name() : null)")
	StaffResponse toResponse(Staff s);
	
	@Mapping(target = "departmentName", source = "department.name")
	@Mapping(target = "gender", expression = "java(s.getGender() != null ? s.getGender().name() : null)")
	@Mapping(target = "status", expression = "java(s.getStatus() != null ? s.getStatus().name() : null)")
	StaffSummaryResponse toSummaryResponse(Staff s);
	
	@Mapping(target = "headId", source = "head.id")
	@Mapping(target = "headName",
			expression = "java(d.getHead() != null ? d.getHead().getFirstName() + ' ' + d.getHead().getLastName() : "+
			             "null)")
	DepartmentResponse toDepartmentResponse(Department d);
	
	@Mapping(target = "staffId", source = "staff.id")
	@Mapping(target = "staffName", expression = "java(l.getStaff().getFirstName() + ' ' + l.getStaff().getLastName())")
	@Mapping(target = "leaveType", expression = "java(l.getLeaveType().name())")
	@Mapping(target = "status", expression = "java(l.getStatus().name())")
	@Mapping(target = "approvedBy",
			expression = "java(l.getApprovedBy() != null ? l.getApprovedBy().getUsername() : null)")
	LeaveRequestResponse toLeaveResponse(LeaveRequest l);
	
	@Mapping(target = "staffId", source = "staff.id")
	@Mapping(target = "staffName", expression = "java(p.getStaff().getFirstName() + ' ' + p.getStaff().getLastName())")
	PayrollResponse toPayrollResponse(Payroll p);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "user", ignore = true)
	@Mapping(target = "department", ignore = true)
	@Mapping(target = "photo", ignore = true)
	@Mapping(target = "status", ignore = true)
	Staff toEntity(CreateStaffRequest request);
}
package com.skooly.mapper;
import com.skooly.dto.request.CreateFeeCategoryRequest;
import com.skooly.dto.request.CreateFeeStructureRequest;
import com.skooly.dto.request.RecordFeePaymentRequest;
import com.skooly.dto.response.FeeCategoryResponse;
import com.skooly.dto.response.FeePaymentResponse;
import com.skooly.dto.response.FeeStructureResponse;
import com.skooly.model.FeeCategory;
import com.skooly.model.FeePayment;
import com.skooly.model.FeeStructure;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeeMapper {
	FeeCategoryResponse toCategoryResponse(FeeCategory feeCategory);
	
	@Mapping(target = "classId", source = "schoolClass.id")
	@Mapping(target = "className", source = "schoolClass.name")
	@Mapping(target = "feeCategoryId", source = "feeCategory.id")
	@Mapping(target = "feeCategoryName", source = "feeCategory.name")
	FeeStructureResponse toStructureResponse(FeeStructure feeStructure);
	
	@Mapping(target = "studentId", source = "student.id")
	@Mapping(target = "studentName",
			expression = "java(p.getStudent().getFirstName() + ' ' + p.getStudent().getLastName())")
	@Mapping(target = "feeStructureId", source = "feeStructure.id")
	@Mapping(target = "feeCategoryName", source = "feeStructure.feeCategory.name")
	@Mapping(target = "paymentMode", expression = "java(p.getPaymentMode().name())")
	@Mapping(target = "status", expression = "java(p.getStatus().name())")
	FeePaymentResponse toPaymentResponse(FeePayment p);
	
	@Mapping(target = "id", ignore = true)
	FeeCategory toCategoryEntity(CreateFeeCategoryRequest request);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "schoolClass", ignore = true)
	@Mapping(target = "feeCategory", ignore = true)
	FeeStructure toStructureEntity(CreateFeeStructureRequest request);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "student", ignore = true)
	@Mapping(target = "feeStructure", ignore = true)
	FeePayment toPaymentEntity(RecordFeePaymentRequest request);
}
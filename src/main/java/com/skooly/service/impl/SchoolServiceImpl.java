package com.skooly.service.impl;

import com.skooly.constant.Status;
import com.skooly.dto.request.SchoolRequest;
import com.skooly.dto.response.SchoolResponse;
import com.skooly.exception.BadRequestException;
import com.skooly.exception.ResourceNotFoundException;
import com.skooly.model.School;
import com.skooly.repository.SchoolRepository;
import com.skooly.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;

    public List<SchoolResponse> getAllSchools() {
        return schoolRepository.findAll().stream()
            .map(SchoolResponse::from)
            .toList();
    }

    public SchoolResponse getSchoolById(Long id) {
        School school = schoolRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("School", id));
        return SchoolResponse.from(school);
    }

    public SchoolResponse createSchool(SchoolRequest request) {
        if (schoolRepository.existsByCode(request.getCode())) {
            throw new BadRequestException("School with code '" + request.getCode() + "' already exists");
        }
        School school = School.builder()
            .name(request.getName())
            .code(request.getCode().toUpperCase())
            .address(request.getAddress())
            .phone(request.getPhone())
            .email(request.getEmail())
            .logo(request.getLogo())
            .status(Status.ACTIVE)
            .build();
        return SchoolResponse.from(schoolRepository.save(school));
    }

    public SchoolResponse updateSchool(Long id, SchoolRequest request) {
        School school = schoolRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("School", id));
        school.setName(request.getName());
        school.setAddress(request.getAddress());
        school.setPhone(request.getPhone());
        school.setEmail(request.getEmail());
        school.setLogo(request.getLogo());
        return SchoolResponse.from(schoolRepository.save(school));
    }

    public void deleteSchool(Long id) {
        if (!schoolRepository.existsById(id)) {
            throw new ResourceNotFoundException("School", id);
        }
        schoolRepository.deleteById(id);
    }
}

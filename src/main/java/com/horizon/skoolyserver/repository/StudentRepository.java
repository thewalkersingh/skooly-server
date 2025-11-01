package com.horizon.skoolyserver.repository;
import com.horizon.skoolyserver.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
	List<Student> findBySchoolClassId(Long classId);
	
	List<Student> findByParentId(Long parentId);
}
package com.horizon.skoolyserver.repository;
import com.horizon.skoolyserver.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
	Teacher findByEmployeeCode(String employeeCode);
}
package com.skooly.repository;
import com.skooly.model.Payroll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
	Optional<Payroll> findByStaffIdAndMonthAndYear(Long staffId, Integer month, Integer year);
	
	boolean existsByStaffIdAndMonthAndYear(Long staffId, Integer month, Integer year);
	
	List<Payroll> findByStaffId(Long staffId);
	
	Page<Payroll> findByMonthAndYear(Integer month, Integer year, Pageable pageable);
	
	@Query("""
			    SELECT SUM(p.netSalary) FROM Payroll p
			    WHERE p.month = :month AND p.year = :year
			""")
	BigDecimal sumNetSalaryByMonthYear(@Param("month") int month, @Param("year") int year);
}
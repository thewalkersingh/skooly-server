package com.skooly.repository;
import com.skooly.model.FeePayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FeePaymentRepository extends JpaRepository<FeePayment, Long> {
	Page<FeePayment> findByStudentId(Long studentId, Pageable pageable);
	
	Page<FeePayment> findByStatus(FeePayment.PaymentStatus status, Pageable pageable);
	
	@Query("""
			    SELECT p FROM FeePayment p
			    WHERE MONTH(p.paymentDate) = :month
			    AND YEAR(p.paymentDate) = :year
			""")
	Page<FeePayment> findByMonthAndYear(
			@Param("month") int month,
			@Param("year") int year,
			Pageable pageable
	                                   );
	
	@Query("""
			    SELECT SUM(p.amountPaid) FROM FeePayment p
			    WHERE p.status = 'PAID'
			    AND MONTH(p.paymentDate) = :month
			    AND YEAR(p.paymentDate) = :year
			""")
	BigDecimal sumCollectedByMonthYear(@Param("month") int month, @Param("year") int year);
	
	@Query("""
			    SELECT p FROM FeePayment p
			    WHERE p.student.id = :studentId
			    AND p.status IN ('PENDING', 'OVERDUE', 'PARTIAL')
			""")
	List<FeePayment> findPendingByStudent(@Param("studentId") Long studentId);
	
	@Query("""
			    SELECT p FROM FeePayment p
			    WHERE p.status IN ('PENDING', 'OVERDUE')
			""")
	List<FeePayment> findAllDefaulters(Pageable pageable);
	
	@Query("""
			    SELECT p FROM FeePayment p
			    WHERE (:studentId IS NULL OR p.student.id = :studentId)
			    AND (:status IS NULL OR p.status = :status)
			    AND (:month IS NULL OR MONTH(p.paymentDate) = :month)
			    AND (:year IS NULL OR YEAR(p.paymentDate) = :year)
			""")
	Page<FeePayment> findWithFilters(
			@Param("studentId") Long studentId,
			@Param("status") FeePayment.PaymentStatus status,
			@Param("month") Integer month,
			@Param("year") Integer year,
			Pageable pageable
	                                );
}
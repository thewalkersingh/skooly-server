package com.skooly.model;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fee_payments")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class FeePayment extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fee_structure_id", nullable = false)
	private FeeStructure feeStructure;
	
	@Column(name = "amount_paid", nullable = false, precision = 10, scale = 2)
	private BigDecimal amountPaid;
	
	@Column(name = "payment_date", nullable = false)
	private LocalDate paymentDate;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "payment_mode", nullable = false)
	private PaymentMode paymentMode;
	
	@Column(name = "transaction_id", length = 100)
	private String transactionId;
	
	@Enumerated(EnumType.STRING)
	private PaymentStatus status = PaymentStatus.PAID;
	
	public enum PaymentMode { CASH, ONLINE, CHEQUE, DD }
	public enum PaymentStatus { PAID, PENDING, OVERDUE, PARTIAL }
}
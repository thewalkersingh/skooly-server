package com.skooly.repository;

import com.skooly.entity.OtpRecord;
import com.skooly.enums.OtpPurpose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRecordRepository extends JpaRepository<OtpRecord, Long> {
	
	// Latest unused OTP for a user + purpose
	@Query("""
		SELECT o FROM OtpRecord o
		WHERE o.user.id = :userId
		AND o.purpose = :purpose
		AND o.used = false
		AND o.expiresAt > CURRENT_TIMESTAMP
		ORDER BY o.createdAt DESC
		""")
	Optional<OtpRecord> findLatestValidOtp(@Param("userId") Long userId, @Param("purpose") OtpPurpose purpose);
	
	// Mark all previous OTPs for same user+purpose as used before sending new one
	@Modifying
	@Query("""
		UPDATE OtpRecord o SET o.used = true
		WHERE o.user.id = :userId
		AND o.purpose = :purpose
		AND o.used = false
		""")
	void invalidatePreviousOtps(@Param("userId") Long userId, @Param("purpose") OtpPurpose purpose);
	
	// Cleanup — delete expired OTPs
	@Modifying
	@Query("DELETE FROM OtpRecord o WHERE o.expiresAt < CURRENT_TIMESTAMP")
	void deleteAllExpired();
	
}
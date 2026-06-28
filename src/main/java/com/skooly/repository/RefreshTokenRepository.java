package com.skooly.repository;

import com.skooly.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	
	Optional<RefreshToken> findByToken(String token);
	
	// Revoke all tokens for a user — used on logout and password reset
	@Modifying
	@Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.user.id = :userId")
	void revokeAllByUserId(@Param("userId") Long userId);
	
	// Delete expired tokens — for cleanup job later
	@Modifying
	@Query("DELETE FROM RefreshToken rt WHERE rt.expiresAt < CURRENT_TIMESTAMP")
	void deleteAllExpired();
	
	// Check if user has any active (non-revoked, non-expired) token
	@Query("SELECT COUNT(rt) > 0 FROM RefreshToken rt WHERE rt.user.id = :userId AND rt.revoked = false AND rt" +
		       ".expiresAt > CURRENT_TIMESTAMP")
	boolean hasActiveToken(@Param("userId") Long userId);
	
}
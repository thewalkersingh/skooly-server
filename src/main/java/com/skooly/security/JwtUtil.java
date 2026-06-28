package com.skooly.security;

import com.skooly.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.access-token-expiry}")
	private long accessTokenExpiry;
	
	@Value("${jwt.refresh-token-expiry}")
	private long refreshTokenExpiry;
	
	// ── Key ───────────────────────────────────────────────────────────────────
	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}
	
	// ── Generate ──────────────────────────────────────────────────────────────
	public String generateAccessToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", user.getRole().name());
		claims.put("roleEntityId", user.getRoleEntityId());
		claims.put("userId", user.getId());
		claims.put("status", user.getStatus().name());
		
		return Jwts.builder()
		           .claims(claims)
		           .subject(user.getIdentity().getEmail() != null
			                    ? user.getIdentity().getEmail()
			                    : user.getIdentity().getPhone())   // email or phone as subject
		           .issuedAt(new Date())
		           .expiration(new Date(System.currentTimeMillis() + accessTokenExpiry))
		           .signWith(getSigningKey())
		           .compact();
	}
	
	public String generateRefreshToken(User user) {
		return Jwts.builder()
		           .subject(String.valueOf(user.getId()))     // userId as subject
		           .issuedAt(new Date())
		           .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiry))
		           .signWith(getSigningKey())
		           .compact();
	}
	
	// ── Extract ───────────────────────────────────────────────────────────────
	public Claims extractAllClaims(String token) {
		return Jwts.parser()
		           .verifyWith(getSigningKey())
		           .build()
		           .parseSignedClaims(token)
		           .getPayload();
	}
	
	public String extractSubject(String token) {
		return extractAllClaims(token).getSubject();
	}
	
	public String extractRole(String token) {
		return extractAllClaims(token).get("role", String.class);
	}
	
	public Long extractUserId(String token) {
		return extractAllClaims(token).get("userId", Long.class);
	}
	
	public Long extractRoleEntityId(String token) {
		return extractAllClaims(token).get("roleEntityId", Long.class);
	}
	
	public Date extractExpiration(String token) {
		return extractAllClaims(token).getExpiration();
	}
	
	// ── Validate ──────────────────────────────────────────────────────────────
	public boolean isTokenValid(String token, User user) {
		try {
			String subject = extractSubject(token);
			String userIdentifier = user.getIdentity().getEmail() != null
				                        ? user.getIdentity().getEmail()
				                        : user.getIdentity().getPhone();
			return subject.equals(userIdentifier) && !isTokenExpired(token);
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	// Used for refresh token validation — subject is userId
	public boolean isRefreshTokenValid(String token, Long userId) {
		try {
			String subject = extractSubject(token);
			return subject.equals(String.valueOf(userId)) && !isTokenExpired(token);
		} catch (Exception e) {
			return false;
		}
	}
	
}
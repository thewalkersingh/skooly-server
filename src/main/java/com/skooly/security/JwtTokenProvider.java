package com.skooly.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
	@Value("${jwt.secret}")
	private String jwtSecret;
	
	@Value("${jwt.expiration}")
	private long jwtExpiration;
	
	@Value("${jwt.refresh-expiration}")
	private long refreshExpiration;
	
	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(jwtSecret.getBytes());
	}
	
	public String generateToken(Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		return buildToken(userPrincipal.getId().toString(), jwtExpiration);
	}
	
	public String generateRefreshToken(Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		return buildToken(userPrincipal.getId().toString(), refreshExpiration);
	}
	
	public String generateTokenFromUserId(Long userId) {
		return buildToken(userId.toString(), jwtExpiration);
	}
	
	public Long getUserIdFromToken(String token) {
		String subject = Jwts.parserBuilder()
				                 .setSigningKey(getSigningKey())
				                 .build()
				                 .parseClaimsJws(token)
				                 .getBody()
				                 .getSubject();
		return Long.parseLong(subject);
	}
	
	public boolean validateToken(String token) {
		try{
			Jwts.parserBuilder()
					.setSigningKey(getSigningKey())
					.build()
					.parseClaimsJws(token);
			return true;
		} catch(ExpiredJwtException e){
			log.warn("JWT token expired: {}", e.getMessage());
		} catch(UnsupportedJwtException e){
			log.warn("JWT token unsupported: {}", e.getMessage());
		} catch(MalformedJwtException e){
			log.warn("JWT token malformed: {}", e.getMessage());
		} catch(IllegalArgumentException e){
			log.warn("JWT token illegal argument: {}", e.getMessage());
		}
		return false;
	}
	
	public long getExpirationMs() {
		return jwtExpiration;
	}
	
	// ── Private helpers ──────────────────────────────────────────────────────
	
	private String buildToken(String subject, long expiration) {
		Date now = new Date();
		Date expiry = new Date(now.getTime()+expiration);
		return Jwts.builder()
				       .setSubject(subject)
				       .setIssuedAt(now)
				       .setExpiration(expiry)
				       .signWith(getSigningKey(), SignatureAlgorithm.HS512)
				       .compact();
	}
}
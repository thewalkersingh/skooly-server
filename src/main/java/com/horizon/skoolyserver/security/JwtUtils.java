package com.horizon.skoolyserver.security;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {
   @Value("${app.jwt.secret}")
   private String jwtSecret;
   
   @Value("${app.jwt.expirationMs}")
   private int jwtExpirationMs;
   
   private Key getSigningKey() {
	  // decode the Base64-encoded secret into a proper key
	  byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
	  return Keys.hmacShaKeyFor(keyBytes);
   }
   
   public String generateToken(UserDetails userDetails) {
	  return Jwts.builder()
					 .setSubject(userDetails.getUsername())
					 .claim("roles", userDetails.getAuthorities().stream()
											 .map(GrantedAuthority::getAuthority).toList())
					 .setIssuedAt(new Date())
					 .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
					 .signWith(getSigningKey(), SignatureAlgorithm.HS512) // ✅ new syntax
					 .compact();
   }
   
   public String getUsernameFromToken(String token) {
	  return Jwts.parserBuilder()
					 .setSigningKey(getSigningKey()) // ✅ parserBuilder instead of parser()
					 .build()
					 .parseClaimsJws(token)
					 .getBody()
					 .getSubject();
   }
   
   public boolean validateToken(String token) {
	  try{
		 Jwts.parserBuilder()
				 .setSigningKey(getSigningKey())
				 .build()
				 .parseClaimsJws(token);
		 return true;
	  } catch(JwtException | IllegalArgumentException e){
		 return false;
	  }
   }
}
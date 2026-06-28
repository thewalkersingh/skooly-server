package com.skooly.security;

import com.skooly.entity.User;
import com.skooly.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
	
	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		
		final String authHeader = request.getHeader("Authorization");
		
		// ── No token — pass through (public endpoints handled by Security config)
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		final String token = authHeader.substring(7);   // strip "Bearer "
		
		try {
			// ── Extract userId from token claims
			Long userId = jwtUtil.extractUserId(token);
			
			// ── Only process if not already authenticated
			if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				
				User user = userRepository.findById(userId).orElse(null);
				
				if (user != null && jwtUtil.isTokenValid(token, user)) {
					
					CustomUserDetails userDetails = new CustomUserDetails(user);
					
					// ── Build authentication token
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null,                           // no credentials needed post-auth
						userDetails.getAuthorities());
					
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					
					// ── Set in security context — request is now authenticated
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
			
		} catch (Exception e) {
			// ── Invalid token — log and continue without setting auth
			// Security config will handle the 401 response
			log.warn("JWT validation failed for request {}: {}", request.getRequestURI(), e.getMessage());
		}
		
		filterChain.doFilter(request, response);
	}
	
	// ── Skip filter for auth endpoints — no token needed there
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getServletPath();
		return path.startsWith("/auth/login") || path.startsWith("/auth/register") || path.startsWith(
			"/auth/verify-otp") || path.startsWith("/auth/refresh") || path.startsWith(
			"/auth/forgot-password") || path.startsWith("/auth/resend-otp");
	}
	
}
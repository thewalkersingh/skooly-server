package com.skooly.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity          // enables @PreAuthorize on controllers
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtAuthFilter jwtAuthFilter;
	private final UserDetailsServiceImpl userDetailsService;
	
	@Value("${cors.allowed-origins}")
	private String allowedOrigins;
	
	// ── Security Filter Chain ─────────────────────────────────────────────────
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
			// Disable CSRF — we use JWT, not sessions
			.csrf(AbstractHttpConfigurer::disable)
			
			// CORS config
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			
			// Session management — stateless, no HttpSession
			.sessionManagement(session ->
				                   session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			
			// Request authorization rules
			.authorizeHttpRequests(
				auth -> auth
					        // ── Swagger — permit in dev ───────────────────────────────────────
					        .requestMatchers(
						        "/swagger-ui/**",
						        "/swagger-ui.html",
						        "/swagger-ui/index.html",
						        "/api-docs/**",
						        "/v3/api-docs/**",
						        "/v3/api-docs",
						        "/swagger-resources/**",
						        "/webjars/**"
					        ).permitAll()
					        // ── Public endpoints — no token needed ────────────────────────
					        .requestMatchers(
						        "/auth/login",
						        "/auth/register",
						        "/auth/verify-otp",
						        "/auth/refresh",
						        "/auth/forgot-password",
						        "/auth/resend-otp",
						        "/auth/reset-password"
					        ).permitAll()
					        
					        // ── Admin only ────────────────────────────────────────────────
					        .requestMatchers(
						        "/auth/create-account",
						        "/auth/approve/**",
						        "/auth/reject/**"
					        ).hasRole("ADMIN")
					        
					        // ── School management — ADMIN only ────────────────────────────
					        .requestMatchers(HttpMethod.POST, "/schools/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.PUT, "/schools/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.DELETE, "/schools/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.PATCH, "/schools/**").hasRole("ADMIN")
					        
					        // ── Classroom management — ADMIN only ─────────────────────────
					        .requestMatchers(HttpMethod.POST, "/classrooms/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.PUT, "/classrooms/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.DELETE, "/classrooms/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.PATCH, "/classrooms/**").hasRole("ADMIN")
					        
					        // ── Section management — ADMIN only ───────────────────────────
					        .requestMatchers(HttpMethod.POST, "/sections/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.PUT, "/sections/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.DELETE, "/sections/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.PATCH, "/sections/**").hasRole("ADMIN")
					        
					        // ── Subject management — ADMIN only ───────────────────────────
					        .requestMatchers(HttpMethod.POST, "/subjects/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.PUT, "/subjects/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.DELETE, "/subjects/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.PATCH, "/subjects/**").hasRole("ADMIN")
					        
					        // ── Teacher management — ADMIN only ───────────────────────────
					        .requestMatchers(HttpMethod.POST, "/teachers/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.PUT, "/teachers/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.DELETE, "/teachers/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.PATCH, "/teachers/**").hasRole("ADMIN")
					        
					        // ── Student management — ADMIN only ───────────────────────────
					        .requestMatchers(HttpMethod.POST, "/students/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.PUT, "/students/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.DELETE, "/students/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.PATCH, "/students/**").hasRole("ADMIN")
					        
					        // ── Parent management — ADMIN only ────────────────────────────
					        .requestMatchers(HttpMethod.POST, "/parents/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.PUT, "/parents/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.DELETE, "/parents/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.PATCH, "/parents/**").hasRole("ADMIN")
					        
					        // ── Staff management — ADMIN only ─────────────────────────────
					        .requestMatchers(HttpMethod.POST, "/staff/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.PUT, "/staff/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.DELETE, "/staff/**").hasRole("ADMIN")
					        .requestMatchers(HttpMethod.PATCH, "/staff/**").hasRole("ADMIN")
					        
					        // ── Notifications — authenticated users only ──────────────────
					        .requestMatchers("/notifications/**").authenticated()
					        
					        // ── GET endpoints — authenticated users (RBAC via @PreAuthorize)
//					        .requestMatchers(HttpMethod.GET, "/**").authenticated()
					        
					        // Schools — allow ADMIN for all methods
					        .requestMatchers("/schools/**").hasRole("ADMIN")
					        
					        // Generic GET fallback
					        .requestMatchers(HttpMethod.GET, "/**").authenticated()
					        
					        
					        // ── Everything else — deny ────────────────────────────────────
					        .anyRequest().denyAll()
			
			)
			
			// Register our JWT filter before Spring's default auth filter
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
			
			// Register our auth provider
			.authenticationProvider(authenticationProvider());
		
		return http.build();
	}
	
	// ── CORS ──────────────────────────────────────────────────────────────────
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		
		CorsConfiguration config = new CorsConfiguration();
		
		// Split by comma — supports multiple origins
		List<String> origins = List.of(allowedOrigins.split(","));
		config.setAllowedOrigins(origins);   // http://localhost:5173
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
		config.setExposedHeaders(List.of("Authorization"));
		config.setAllowCredentials(true);
		config.setMaxAge(3600L);    // cache preflight for 1 hour
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
	
	// ── Authentication Provider ───────────────────────────────────────────────
	@Bean
	public AuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	
	// ── Password Encoder ──────────────────────────────────────────────────────
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	// ── Authentication Manager ────────────────────────────────────────────────
	// Exposed as bean — used in AuthService to authenticate login requests
	@Bean
	public AuthenticationManager authenticationManager(
		AuthenticationConfiguration config) throws Exception {
		
		return config.getAuthenticationManager();
	}
	
}
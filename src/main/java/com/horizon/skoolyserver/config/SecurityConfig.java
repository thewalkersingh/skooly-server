package com.horizon.skoolyserver.config;
import com.horizon.skoolyserver.security.CustomUserDetailsService;
import com.horizon.skoolyserver.security.JwtAuthFilter;
import com.horizon.skoolyserver.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
   private final JwtUtils jwtUtils;
   private final CustomUserDetailsService userDetailsService;
   
   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	  JwtAuthFilter jwtFilter = new JwtAuthFilter(jwtUtils, userDetailsService);
	  
	  http.csrf().disable()
			  .cors()
			  .and()
			  .authorizeHttpRequests()
			  .requestMatchers("/api/auth/**",// login, signup
							   "/api/students/**",// any other open endpoints
							   "/api/teachers/**", // any other open endpoints
							   "/h2-console/**" // dev only
							  )
			  .permitAll()
			  // Role-based endpoints
			  .requestMatchers("/api/admin/**").hasRole("ADMIN")
			  .requestMatchers("/api/teacher/**").hasRole("TEACHER")
			  .requestMatchers("/api/parent/**").hasRole("PARENT")
			  
			  // Everything else requires authentication
			  .anyRequest().authenticated()
			  .and()
			  .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			  .and()
			  .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	  
	  // Allow H2 console frames (optional)
	  http.headers().frameOptions().disable();
	  
	  return http.build();
   }
   
   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	  return config.getAuthenticationManager();
   }
   
   @Bean
   public PasswordEncoder passwordEncoder() {
	  return new BCryptPasswordEncoder();
   }
}

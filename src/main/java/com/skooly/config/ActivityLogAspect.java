package com.skooly.config;
import com.skooly.model.ActivityLog;
import com.skooly.security.UserPrincipal;
import com.skooly.service.ActivityLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ActivityLogAspect {
	private final ActivityLogService activityLogService;
	
	// Auto-log all POST (CREATE) requests
	@AfterReturning(
			pointcut = "execution(* com.skooly.controller.*.*(..)) && @annotation(org.springframework.web.bind.annotation" +
			           ".PostMapping)",
			returning = "result"
	)
	public void logCreate(JoinPoint joinPoint, Object result) {
		logActivity(joinPoint, ActivityLog.Action.CREATE);
	}
	
	// Auto-log all PUT/PATCH (UPDATE) requests
	@AfterReturning(
			pointcut = "execution(* com.skooly.controller.*.*(..)) && ("+
			           "@annotation(org.springframework.web.bind.annotation.PutMapping) || "+
			           "@annotation(org.springframework.web.bind.annotation.PatchMapping))",
			returning = "result"
	)
	public void logUpdate(JoinPoint joinPoint, Object result) {
		logActivity(joinPoint, ActivityLog.Action.UPDATE);
	}
	
	// Auto-log all DELETE requests
	@AfterReturning(
			pointcut = "execution(* com.skooly.controller.*.*(..)) && @annotation(org.springframework.web.bind.annotation" +
			           ".DeleteMapping)",
			returning = "result"
	)
	public void logDelete(JoinPoint joinPoint, Object result) {
		logActivity(joinPoint, ActivityLog.Action.DELETE);
	}
	
	// ── Private helpers ──────────────────────────────────────────────────────
	
	private void logActivity(JoinPoint joinPoint, ActivityLog.Action action) {
		try{
			Long userId = getCurrentUserId();
			String module = extractModule(joinPoint);
			String desc = action.name()+" via "+joinPoint.getSignature().getName();
			String ip = getClientIp();
			activityLogService.log(userId, action, module, desc, ip);
		} catch(Exception e){
			log.warn("Activity log aspect failed: {}", e.getMessage());
		}
	}
	
	private Long getCurrentUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null && auth.getPrincipal() instanceof UserPrincipal principal){
			return principal.getId();
		}
		return null;
	}
	
	private String extractModule(JoinPoint joinPoint) {
		// Extract module name from controller class name
		// e.g. StudentController -> STUDENT
		String className = joinPoint.getTarget().getClass().getSimpleName();
		return className.replace("Controller", "").toUpperCase();
	}
	
	private String getClientIp() {
		try{
			ServletRequestAttributes attrs =
					(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if(attrs != null){
				HttpServletRequest request = attrs.getRequest();
				String ip = request.getHeader("X-Forwarded-For");
				return (ip != null && !ip.isEmpty()) ? ip.split(",")[0] : request.getRemoteAddr();
			}
		} catch(Exception ignored){ }
		return "unknown";
	}
}
package ecommerce.coupang.aop.log;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingAspect {

	private final RequestUUID requestUUID;
	private final HttpServletRequest request;

	@Pointcut("execution(* ecommerce.coupang.controller..*(..))")
	public void controller() {}

	@Pointcut("execution(* ecommerce.coupang.service..*(..))")
	public void service() {}

	@Pointcut("execution(* ecommerce.coupang.repository..*(..))")
	public void repository() {}

	@Around("controller()")
	public Object loggingController(ProceedingJoinPoint joinPoint) throws Throwable {
		String httpMethod = request.getMethod();
		String url = request.getRequestURI();

		Member member = getPrincipal();
		String user = member != null ? "[회원] ID: " + member.getId() : "[비회원]";

		long startTime = System.currentTimeMillis();

		log.info("[Controller-{}] {} METHOD: {} 요청 URL: {}", requestUUID.getRequestId(), user, httpMethod, url);

		try {
			Object result = joinPoint.proceed();
			long endTime = System.currentTimeMillis();

			log.info("[Controller-{}] {} METHOD: {} 요청 URL: {} - 정상 종료, 실행 시간: {}ms", requestUUID.getRequestId(), user, httpMethod, url, endTime - startTime);

			return result;
		} catch (Throwable e) {
			long endTime = System.currentTimeMillis();

			log.info("[Controller-{}] {} METHOD: {} 요청 URL: {} - 이상 종료, 실행 시간: {}ms", requestUUID.getRequestId(), user, httpMethod, url, endTime - startTime);
			throw e;
		}
	}

	@Around("service()")
	public Object loggingService(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
		Object target = joinPoint.getTarget();
		Class<?> targetClass = target.getClass();

		String serviceName = getServiceName(targetClass);
		String methodName = getLogActionName(targetClass, methodSignature.getMethod());

		Member member = getPrincipal();
		String user = member != null ? "[회원] ID: " + member.getId() : "[비회원]";
		long startTime = System.currentTimeMillis();

		log.info("  [{}-{}] {} 작업: {} 호출", serviceName, requestUUID.getRequestId(), user, methodName);

		try {
			Object result = joinPoint.proceed();

			long endTime = System.currentTimeMillis();
			if (result instanceof Long)
				log.info("  [{}-{}] {} 작업: {} ID: {} - 정상 종료, 실행 시간: {}ms", serviceName, requestUUID.getRequestId(), user, methodName, result,
					endTime - startTime);
			else
				log.info("  [{}-{}] {} 작업: {} - 정상 종료, 실행 시간: {}ms", serviceName, requestUUID.getRequestId(), user, methodName,
					endTime - startTime);

			return result;
		} catch (Throwable e) {
			long endTime = System.currentTimeMillis();
			String errorCode = e instanceof CustomException ? ((CustomException) e).getError() : "UNKNOWN_ERROR";

			log.error("  [{}-{}] {} 작업: {} - 실패 {}, 실행 시간: {}ms", serviceName, requestUUID.getRequestId(), user, methodName, errorCode, endTime - startTime);
			throw e;
		}
	}

	@Around("repository()")
	public Object logRepository(ProceedingJoinPoint joinPoint) throws Throwable {
		String methodName = joinPoint.getSignature().getName();
		Object target = joinPoint.getTarget();
		Class<?> targetClass = target.getClass();
		String repositoryName = getRepositoryName(targetClass);

		long startTime = System.currentTimeMillis();

		try {
			Object result = joinPoint.proceed();
			long endTime = System.currentTimeMillis();
			log.info("    [{}-{}] {}() - 정상 종료, 실행 시간: {}ms", repositoryName, requestUUID.getRequestId(), methodName, endTime - startTime);
			return result;
		} catch (Throwable e) {
			long endTime = System.currentTimeMillis();
			log.error("    [{}-{}] {}() - 이상 종료, 실행 시간: {}ms", repositoryName, requestUUID.getRequestId(), methodName, endTime - startTime);
			throw e;
		}
	}

	private String getServiceName(Class<?> targetClass) {
		for (Class<?> iface : targetClass.getInterfaces()) {
			LogLevel logLevel = iface.getAnnotation(LogLevel.class);
			if (logLevel != null) {
				return logLevel.value();
			}
		}
		return targetClass.getSimpleName();
	}

	private String getLogActionName(Class<?> targetClass, Method method) {
		LogAction logAction = null;
		for (Class<?> iface : targetClass.getInterfaces()) {
			try {
				Method ifaceMethod = iface.getMethod(method.getName(), method.getParameterTypes());
				logAction = ifaceMethod.getAnnotation(LogAction.class);
				if (logAction != null) {
					return logAction.value();
				}
			} catch (NoSuchMethodException e) {
				// 해당 인터페이스에 해당 메서드가 없으면 무시하고 다음 인터페이스 확인
			}
		}
		return null;
	}

	private String getRepositoryName(Class<?> targetClass) {
		for (Class<?> iface : targetClass.getInterfaces()) {
			LogLevel logLevel = iface.getAnnotation(LogLevel.class);
			if (logLevel != null) {
				return logLevel.value();
			}
		}
		return targetClass.getSimpleName();
	}

	private Member getPrincipal() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails))
			return null;

        return userDetails.getMember();
	}
}
package iGuard.Server.Component;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    @Around("execution(* iGuard.Server.IGuardServerApplication.run(..)) || execution(* iGuard.Server.Service.admin.CSVImportService.import*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed(); // 메서드 실행

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("메서드: {} 실행 시간: {}ms", joinPoint.getSignature(), duration);

        return result;
    }
}


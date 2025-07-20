package com.tradin.core.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class AopForTransaction {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error(
                "🚫 트랜잭션 처리 중 오류 발생 - method: {}, error: {}",
                joinPoint.getSignature().getName(),
                throwable.getMessage(),
                throwable
            );
            throw throwable;
        } finally {
//            log.info("🔓 트랜잭션 종료 - method: {}", joinPoint.getSignature().getName());
        }
    }
}
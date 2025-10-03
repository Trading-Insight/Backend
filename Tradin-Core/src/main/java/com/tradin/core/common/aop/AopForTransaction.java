package com.tradin.core.common.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
@RequiredArgsConstructor
@Slf4j
public class AopForTransaction {
    private final PlatformTransactionManager transactionManager;

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
        }
    }

    /**
     * 트랜잭션을 새로 생성하고, 지정된 시간(초) 내에 완료되지 않으면 롤백합니다.
     */
    public Object proceedWithTimeout(final ProceedingJoinPoint joinPoint, int timeoutSeconds) throws Throwable {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        if (timeoutSeconds > 0) {
            transactionDefinition.setTimeout(timeoutSeconds);
        }

        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);

        try {
            Object result = joinPoint.proceed();
            transactionManager.commit(transactionStatus);
            return result;
        } catch (Throwable throwable) {
            transactionManager.rollback(transactionStatus);
            log.error(
                "🚫 트랜잭션 처리 중 오류 발생 - method: {}, timeout: {}s, error: {}",
                joinPoint.getSignature().getName(),
                timeoutSeconds,
                throwable.getMessage(),
                throwable
            );
            throw throwable;
        }
    }
}
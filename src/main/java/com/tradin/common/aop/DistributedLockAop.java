package com.tradin.common.aop;

import static com.tradin.common.exception.ExceptionType.LOCK_ACQUISITION_FAILED_EXCEPTION;
import static com.tradin.common.exception.ExceptionType.NOT_FOUND_SUCH_METHOD_EXCEPTION;

import com.tradin.common.annotation.DistributedLock;
import com.tradin.common.exception.TradinException;
import com.tradin.common.utils.CustomSpringELParser;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";
    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(distributedLock)")
    public Object handleDistributedLock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String key = createKey(joinPoint, distributedLock, signature);
        RLock lock = redissonClient.getLock(key);

        boolean isLocked = false;

        try {
            isLocked = lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());

            if (!isLocked) {
                log.warn("🔒 락 획득 실패 - key: {}", key);
                return invokeFallback(joinPoint, distributedLock.fallbackMethod());
            }

            log.info("🔐 락 획득 성공 - key: {}", key);
            return aopForTransaction.proceed(joinPoint);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("🚫 락 처리 중 인터럽트 발생 - key: {}", key, e);
            return invokeFallback(joinPoint, distributedLock.fallbackMethod());
        } finally {
            if (isLocked && lock.isHeldByCurrentThread()) {
                try {
                    lock.unlock();
                    log.info("🔓 락 해제 - key: {}", key);
                } catch (IllegalMonitorStateException e) {
                    log.warn("⚠️ 락 이미 해제됨 - method: {}, key: {}", method.getName(), key);
                }
            }
        }
    }

    private String createKey(ProceedingJoinPoint joinPoint, DistributedLock distributedLock, MethodSignature signature) {
        return REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(
            signature.getParameterNames(),
            joinPoint.getArgs(),
            distributedLock.key()
        );
    }

    private Object invokeFallback(ProceedingJoinPoint joinPoint, String fallbackMethodName) throws Throwable {
        validateIsMethodEmpty(fallbackMethodName);
        Method fallbackMethod = findFallbackMethod(joinPoint, fallbackMethodName);

        validateIsExistMethod(fallbackMethod);
        Object target = joinPoint.getTarget();

        return fallbackMethod.invoke(target, joinPoint.getArgs());
    }

    private void validateIsMethodEmpty(String fallbackMethodName) {
        if (fallbackMethodName.isEmpty()) {
            throw new TradinException(LOCK_ACQUISITION_FAILED_EXCEPTION);
        }
    }

    private void validateIsExistMethod(Method fallbackMethod) {
        if (fallbackMethod == null) {
            throw new TradinException(NOT_FOUND_SUCH_METHOD_EXCEPTION);
        }
    }

    private Method findFallbackMethod(ProceedingJoinPoint joinPoint, String fallbackMethodName) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> targetClass = joinPoint.getTarget().getClass();

        for (Method method : targetClass.getDeclaredMethods()) {
            if (method.getName().equals(fallbackMethodName)
                && method.getParameterCount() == signature.getParameterNames().length) {
                return method;
            }
        }
        return null;
    }
}
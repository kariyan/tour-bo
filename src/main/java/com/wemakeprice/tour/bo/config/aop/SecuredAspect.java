package com.wemakeprice.tour.bo.config.aop;

import com.wemakeprice.tour.bo.common.util.SecuredProcessor;
import com.wemakeprice.tour.bo.config.secured.MaskingProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SecuredAspect {

    private final MaskingProcessor maskingProcessor;

    @Around("execution(* com.wemakeprice.tour.bo.service.controller..*.*(..))")
    public Object masking(ProceedingJoinPoint joinPoint) throws Throwable {
        Object bean = joinPoint.proceed();
        maskingProcessor.process(bean);
        return bean;
    }

    @Around("execution(* com.wemakeprice.tour.bo.service.store..*Store.*(..))")
    public Object encrypt(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            SecuredProcessor.ENCRYPTION.process(arg);
        }

        Object bean = joinPoint.proceed();
        SecuredProcessor.DECRYPTION.process(bean);
        return bean;
    }
}

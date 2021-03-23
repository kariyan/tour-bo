package com.wemakeprice.tour.bo.config.aop;

import com.wemakeprice.tour.bo.common.entity.AuditLog;
import com.wemakeprice.tour.bo.common.entity.Auditable;
import com.wemakeprice.tour.bo.common.request.ServiceContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditableAspect {

    @Around("execution(* com.wemakeprice.tour.bo.domain.store..*Store.*(..))")
    public Object addAccountIdToAuditLog(ProceedingJoinPoint joinPoint) throws Throwable {
        ServiceContextHolder.ServiceContext context = ServiceContextHolder.getContext();
        String accountId = context.getAccountId();

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Auditable) {
                AuditLog auditLog = ((Auditable) arg).getAuditLog();
                auditLog.setCreatedBy(accountId);
                auditLog.setUpdatedBy(accountId);
            } else if (arg instanceof Collection<?>
                    && ((Collection<?>) arg).stream().allMatch(Auditable.class::isInstance)) {
                ((Collection<?>) arg).forEach(obj -> {
                    AuditLog auditLog = ((Auditable) obj).getAuditLog();
                    auditLog.setCreatedBy(accountId);
                    auditLog.setUpdatedBy(accountId);
                });
            }
        }
        return joinPoint.proceed();
    }
}

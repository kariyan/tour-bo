package com.wemakeprice.tour.bo.common.request;

import com.wemakeprice.tour.bo.domain.entity.auth.enumtype.AuthorityType;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceContextHolder {

    private static final ServiceContextHolder INSTANCE = new ServiceContextHolder();
    private final ThreadLocal<ServiceContext> context = new ThreadLocal<>();

    public static synchronized ServiceContext getContext() {
        ServiceContext ctx = INSTANCE.context.get();
        if (ctx == null) {
            ctx = new ServiceContext();
            INSTANCE.context.set(ctx);
        }

        return ctx;
    }

    public static void clearContext() {
        INSTANCE.context.remove();
    }

    @Getter
    @Setter
    @ToString
    public static class ServiceContext {
        private String accountId;
        private String accountName;
        private Long authorityGroupId;
        private AuthorityType authorityType;
        private String accountEmail;
        private String positionCd;
        private String jobCd;
        private String jobName;
        private String onesDutyCd;
        private String departmentCd;
        private String departmentName;
        private String requestIpAddress;
    }
}

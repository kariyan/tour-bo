package com.wemakeprice.tour.bo.common.request;

import com.wemakeprice.tour.bo.common.util.SecuredProcessor;

import java.util.HashSet;
import java.util.Set;

public class SecuredBeanHolder {

    private static final SecuredBeanHolder instance = new SecuredBeanHolder();
    private final ThreadLocal<Set<SecuredProcessor.BeanStatus>> securedBeans = new ThreadLocal<>();

    private SecuredBeanHolder() {
    }

    public static Set<SecuredProcessor.BeanStatus> getSecuredBeans() {

        Set<SecuredProcessor.BeanStatus> beanStatuses = instance.securedBeans.get();
        if (beanStatuses == null) {
            beanStatuses = new HashSet<>();
            instance.securedBeans.set(beanStatuses);
        }

        return beanStatuses;
    }

    public static void clearContext() {
        instance.securedBeans.remove();
    }
}

package com.wemakeprice.tour.bo.config;

import com.wemakeprice.tour.bo.common.annotation.TestExcluded;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@ComponentScan(
        basePackages = {
                "com.wemakeprice.tour.bo.service.logic.auth",
                "com.wemakeprice.tour.bo.service.store.auth",
                "com.wemakeprice.tour.bo.service.converter.auth",
        },
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = TestExcluded.class))
public class AuthorizationContextConfiguration {
}

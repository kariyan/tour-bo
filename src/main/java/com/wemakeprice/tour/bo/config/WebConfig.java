package com.wemakeprice.tour.bo.config;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;
import com.wemakeprice.tour.bo.config.interceptor.AuthorizationInterceptor;
import com.wemakeprice.tour.bo.config.interceptor.JwtInterceptor;
import com.wemakeprice.tour.bo.config.interceptor.LoggerInterceptor;
import com.wemakeprice.tour.bo.config.interceptor.SecuredInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private static final String[] EXCLUDE_PATHS = new String[]{
            "/v1/auth/login",
    };

    private final JwtInterceptor jwtInterceptor;
    private final LoggerInterceptor loggerInterceptor;
    private final SecuredInterceptor securedInterceptor;
    private final AuthorizationInterceptor authorizationInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggerInterceptor);
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/v1/**").excludePathPatterns(EXCLUDE_PATHS);
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/v1/**").excludePathPatterns(EXCLUDE_PATHS);
        registry.addInterceptor(securedInterceptor);
    }

    @Bean
    public FilterRegistrationBean<XssEscapeServletFilter> filterRegistrationBean() {
        FilterRegistrationBean<XssEscapeServletFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new XssEscapeServletFilter());
        registrationBean.setOrder(1);
        return registrationBean;
    }
}

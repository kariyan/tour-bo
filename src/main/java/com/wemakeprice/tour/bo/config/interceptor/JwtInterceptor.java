package com.wemakeprice.tour.bo.config.interceptor;

import com.wemakeprice.tour.bo.common.annotation.NoLoginRequired;
import com.wemakeprice.tour.bo.common.exception.AuthenticationException;
import com.wemakeprice.tour.bo.common.request.ServiceContextHolder;
import com.wemakeprice.tour.bo.common.sharedtype.JwtAccountInfoDTO;
import com.wemakeprice.tour.bo.common.util.JwtTokenGenerator;
import com.wemakeprice.tour.bo.common.util.ValidationUtils;
import com.wemakeprice.tour.bo.domain.entity.auth.AccLog;
import com.wemakeprice.tour.bo.domain.service.auth.AccLogService;
import com.wemakeprice.tour.bo.domain.service.auth.AccountAuthorityService;
import com.wemakeprice.tour.bo.domain.service.auth.dto.AccountDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;

import static com.wemakeprice.tour.bo.common.exception.AuthenticationException.ErrorMessage.JWT_VALIDATION_ERROR;
import static com.wemakeprice.tour.bo.common.exception.AuthenticationException.ErrorMessage.NO_ACCOUNT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private static final String HEADER_AUTH = "Authorization";

    private final AccLogService accLogService;
    private final AccountAuthorityService accountAuthorityService;

    @Value("${wonder.jwt.secret}")
    private String secret;

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            return true;
        }

        if (handler instanceof HandlerMethod
                && ((HandlerMethod) handler).getMethodAnnotation(NoLoginRequired.class) != null) {
            registerAccessLog(request, null);
            return true;
        }

        JwtAccountInfoDTO jwtAccountInfoDto = parseJwtAccountInfo(request);

        ServiceContextHolder.ServiceContext serviceContext = ServiceContextHolder.getContext();
        serviceContext.setAccountId(jwtAccountInfoDto.getAccountId());
        serviceContext.setAccountName(jwtAccountInfoDto.getAccountName());
        serviceContext.setAccountEmail(jwtAccountInfoDto.getAccountEmail());
        serviceContext.setPositionCd(jwtAccountInfoDto.getPositionCd());
        serviceContext.setJobCd(jwtAccountInfoDto.getJobCd());
        serviceContext.setJobName(jwtAccountInfoDto.getJobName());
        serviceContext.setOnesDutyCd(jwtAccountInfoDto.getOnesDutyCd());
        serviceContext.setDepartmentCd(jwtAccountInfoDto.getDepartmentCd());
        serviceContext.setDepartmentName(jwtAccountInfoDto.getDepartmentName());
        serviceContext.setRequestIpAddress(getRemoteAddr(request));

        AccountDTO accountDTO = accountAuthorityService.findAccountById(jwtAccountInfoDto.getAccountId())
                .orElseThrow(() -> new AuthenticationException(NO_ACCOUNT_FOUND));
        if (accountDTO != null) {
            serviceContext.setAuthorityGroupId(accountDTO.getAuthorityGroupId());
            serviceContext.setAuthorityType(accountDTO.getAuthorityType());
        }

        registerAccessLog(request, serviceContext);
        return true;
    }

    public JwtAccountInfoDTO parseJwtAccountInfo(HttpServletRequest request) {
        String authorization = request.getHeader(HEADER_AUTH);
        if (authorization == null || authorization.isEmpty()) {
            throw new AuthenticationException(JWT_VALIDATION_ERROR);
        }
        final String token = request.getHeader(HEADER_AUTH).replace("Bearer ", "");

        if (!JwtTokenGenerator.isValid(token, secret)) {
            throw new AuthenticationException(JWT_VALIDATION_ERROR);
        }
        JwtAccountInfoDTO jwtAccountInfoDTO = JwtTokenGenerator.get(secret);
        try {
            ValidationUtils.validate(jwtAccountInfoDTO, false);
        } catch (ValidationException e) {
            throw new AuthenticationException(JWT_VALIDATION_ERROR);
        }

        request.setAttribute("claims", jwtAccountInfoDTO);

        return jwtAccountInfoDTO;
    }

    private void registerAccessLog(HttpServletRequest request, ServiceContextHolder.ServiceContext context) {
        if (!"healthcheck".contains(request.getRequestURI())) {
            AccLog accLog = new AccLog();
            accLog.setLogIp(this.getRemoteAddr(request));
            accLog.setRequestType(request.getMethod());
            accLog.setLogUri(request.getRequestURI());
            if (context == null || ObjectUtils.isEmpty(context.getAccountId())) {
                accLog.getAuditLog().setCreatedBy("uncertified");
            } else {
                accLog.getAuditLog().setCreatedBy(context.getAccountId());
            }
            accLogService.registerAccLog(accLog);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ServiceContextHolder.clearContext();
    }

    private String getRemoteAddr(HttpServletRequest request) {
        String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
        if (ipFromHeader != null && ipFromHeader.length() > 0) {
            log.debug("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
            return ipFromHeader;
        }
        return request.getRemoteAddr();
    }
}

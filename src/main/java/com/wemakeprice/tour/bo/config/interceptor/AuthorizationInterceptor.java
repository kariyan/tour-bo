package com.wemakeprice.tour.bo.config.interceptor;

import com.wemakeprice.tour.bo.common.annotation.NoAuthorizationRequired;
import com.wemakeprice.tour.bo.common.annotation.NoLoginRequired;
import com.wemakeprice.tour.bo.common.exception.AuthorizationException;
import com.wemakeprice.tour.bo.common.request.ServiceContextHolder;
import com.wemakeprice.tour.bo.common.request.URIMapping;
import com.wemakeprice.tour.bo.domain.service.auth.MenuService;
import com.wemakeprice.tour.bo.domain.service.auth.dto.NavigationBarDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final MenuService menuService;

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        if (request.getMethod().equalsIgnoreCase("OPTIONS") || !(handler instanceof HandlerMethod)) {
            return true;
        }
        if (((HandlerMethod) handler).getMethodAnnotation(NoLoginRequired.class) != null
                || ((HandlerMethod) handler).getMethodAnnotation(NoAuthorizationRequired.class) != null) {
            return true;
        }

        String requestURI = request.getRequestURI();
        ServiceContextHolder.ServiceContext context = ServiceContextHolder.getContext();
        if (ObjectUtils.isEmpty(context.getAuthorityGroupId()) || ObjectUtils.isEmpty(context.getAccountId())) {
            throw new AuthorizationException(AuthorizationException.ErrorMessage.UNAUTHORIZED);
        }

        Optional<NavigationBarDTO> found =
                menuService.findNavigationBar(context.getAuthorityGroupId(), context.getAccountId()).stream()
                        .filter(navigationBarDTO -> navigationBarDTO.getMenuLevel() >= 2)
                        .filter(navigationBarDTO -> !ObjectUtils.isEmpty(navigationBarDTO.getLink())
                                && requestURI.startsWith(URIMapping.V1_BASE_URI + navigationBarDTO.getLink()))
                        .findAny(); // BE 의 API URI 가 FE link 값으로 시작하는지 검증

        if (!found.isPresent() || !found.get().isReadFlag()) {
            throw new AuthorizationException(AuthorizationException.ErrorMessage.UNAUTHORIZED);
        }

        if (("POST".equals(request.getMethod())
                || "PUT".equals(request.getMethod())
                || "PATCH".equals(request.getMethod())
                || "DELETE".equals(request.getMethod()))
                && !found.get().isWriteFlag()) {
            throw new AuthorizationException(AuthorizationException.ErrorMessage.UNAUTHORIZED);
        }
        return true;
    }
}

package com.wemakeprice.tour.bo.service.controller.common;

import com.wemakeprice.tour.bo.common.annotation.NoAuthorizationRequired;
import com.wemakeprice.tour.bo.common.request.ServiceContextHolder;
import com.wemakeprice.tour.bo.common.request.URIMapping;
import com.wemakeprice.tour.bo.domain.service.auth.MenuService;
import com.wemakeprice.tour.bo.domain.service.auth.dto.NavigationBarDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(URIMapping.V1_BASE_URI + "/navigation-bars")
@RequiredArgsConstructor
public class NavigationBarRestController {

    private final MenuService menuService;

    @Operation(summary = "네비게이션바")
    @GetMapping
    @NoAuthorizationRequired
    public List<NavigationBarDTO> findNavigationBar() {
        ServiceContextHolder.ServiceContext context = ServiceContextHolder.getContext();
        return menuService.findNavigationBar(context.getAuthorityGroupId(), context.getAccountId());
    }
}

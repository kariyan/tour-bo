package com.wemakeprice.tour.bo.service.controller.auth;

import com.wemakeprice.tour.bo.common.request.URIMapping;
import com.wemakeprice.tour.bo.domain.entity.auth.Menu;
import com.wemakeprice.tour.bo.domain.service.auth.MenuService;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.MenuCDO;
import com.wemakeprice.tour.bo.domain.service.auth.dto.MenuRequestDTO;
import com.wemakeprice.tour.bo.domain.service.auth.dto.MenuUseFlagRequestDTO;
import com.wemakeprice.tour.bo.domain.service.auth.key.MenuKey;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(URIMapping.V1_BASE_URI + "/auth/menus")
@RequiredArgsConstructor
public class MenuRestController {
    private final MenuService menuService;

    @Operation(summary = "메뉴 추가")
    @PostMapping
    public long registerMenu(@RequestBody MenuCDO menuCDO) {
        return menuService.registerMenu(menuCDO);
    }

    @Operation(summary = "메뉴 목록 조회")
    @GetMapping
    public List<Menu> findMenusByCondition(MenuKey condition) {
        return menuService.findMenusByCondition(condition);
    }

    @Operation(summary = "메뉴 사용,미사용 처리")
    @PatchMapping("/use-flags")
    public int modifyMenuUseFlag(@RequestBody List<MenuUseFlagRequestDTO> menuUseFlagRequestDTOS) {
        return menuService.modifyMenuUseFlags(menuUseFlagRequestDTOS);
    }

    @Operation(summary = "메뉴 수정")
    @PutMapping("/{menuId:[\\d]+}")
    public boolean modifyMenu(@RequestBody @Valid MenuRequestDTO menuRequestDTO, @PathVariable Long menuId) {
        return menuService.modifyMenu(menuId, menuRequestDTO);
    }

    @Operation(summary = "메뉴 삭제")
    @DeleteMapping("/{menuId:[\\d]+}")
    public boolean modifyMenu(@PathVariable Long menuId) {
        return menuService.removeMenu(menuId);
    }
}

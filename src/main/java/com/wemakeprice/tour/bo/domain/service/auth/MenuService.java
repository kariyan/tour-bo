package com.wemakeprice.tour.bo.domain.service.auth;

import com.wemakeprice.tour.bo.domain.entity.auth.Menu;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.MenuCDO;
import com.wemakeprice.tour.bo.domain.service.auth.dto.MenuRequestDTO;
import com.wemakeprice.tour.bo.domain.service.auth.dto.MenuUseFlagRequestDTO;
import com.wemakeprice.tour.bo.domain.service.auth.dto.NavigationBarDTO;
import com.wemakeprice.tour.bo.domain.service.auth.key.MenuKey;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface MenuService {

    long registerMenu(@Valid MenuCDO menuCDO);

    List<Menu> findMenusByCondition(MenuKey condition);

    int modifyMenuUseFlags(@NotEmpty List<@NotNull @Valid MenuUseFlagRequestDTO> menuUseFlagRequestDTOS);

    boolean modifyMenu(@NotNull Long menuId, @Valid MenuRequestDTO menuRequestDTO);

    boolean removeMenu(@NotNull Long menuId);

    List<NavigationBarDTO> findNavigationBar(@NotNull Long authorityGroupId, @NotBlank String accountId);
}

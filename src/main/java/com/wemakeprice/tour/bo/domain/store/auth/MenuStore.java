package com.wemakeprice.tour.bo.domain.store.auth;

import com.wemakeprice.tour.bo.domain.entity.auth.Menu;
import com.wemakeprice.tour.bo.domain.service.auth.dto.MenuUseFlagRequestDTO;
import com.wemakeprice.tour.bo.domain.service.auth.key.MenuKey;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Validated
public interface MenuStore {

    long insert(@NotNull Menu menu);

    Optional<Menu> selectById(@NotNull Long menuId);

    List<Menu> selectByCondition(MenuKey condition);

    boolean update(@Valid @NotNull Menu menu);

    int updateUseFlags(@NotEmpty List<@Valid MenuUseFlagRequestDTO> menuUseFlagRequestDTOS);
}

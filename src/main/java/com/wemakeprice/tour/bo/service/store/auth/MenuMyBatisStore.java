package com.wemakeprice.tour.bo.service.store.auth;

import com.wemakeprice.tour.bo.common.request.ServiceContextHolder;
import com.wemakeprice.tour.bo.domain.entity.auth.Menu;
import com.wemakeprice.tour.bo.domain.service.auth.dto.MenuUseFlagRequestDTO;
import com.wemakeprice.tour.bo.domain.service.auth.key.MenuKey;
import com.wemakeprice.tour.bo.domain.store.auth.MenuStore;
import com.wemakeprice.tour.bo.service.store.auth.mapper.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MenuMyBatisStore implements MenuStore {
    private final MenuMapper menuMapper;

    @Override
    public long insert(Menu menu) {
        menuMapper.insert(menu);
        return menu.getMenuId();
    }

    @Override
    public Optional<Menu> selectById(Long menuId) {
        return Optional.ofNullable(menuMapper.select(menuId));
    }

    @Override
    public List<Menu> selectByCondition(MenuKey condition) {
        return menuMapper.selectByCondition(condition);
    }

    @Override
    public boolean update(Menu menu) {
        return menuMapper.update(menu) > 0;
    }

    @Override
    public int updateUseFlags(List<MenuUseFlagRequestDTO> menuUseFlagRequestDTOS) {
        Map<Boolean, Set<Long>> menuIdsByUseFlagMap = menuUseFlagRequestDTOS.stream()
                .collect(Collectors.groupingBy(MenuUseFlagRequestDTO::getUseFlag, Collectors.mapping(MenuUseFlagRequestDTO::getMenuId,
                        Collectors.toSet())));

        int count = 0;
        count += updateUseFlags(menuIdsByUseFlagMap, true);
        count += updateUseFlags(menuIdsByUseFlagMap, false);
        return count;
    }

    private int updateUseFlags(Map<Boolean, Set<Long>> menuIdsGroupByUseFlag, boolean useFlag) {
        ServiceContextHolder.ServiceContext context = ServiceContextHolder.getContext();
        if (menuIdsGroupByUseFlag.containsKey(useFlag)) {
            return menuMapper.updateUseFlags(menuIdsGroupByUseFlag.get(useFlag), useFlag, context.getAccountId());
        }
        return 0;
    }
}

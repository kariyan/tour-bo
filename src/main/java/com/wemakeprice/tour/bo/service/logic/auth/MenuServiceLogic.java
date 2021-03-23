package com.wemakeprice.tour.bo.service.logic.auth;

import com.wemakeprice.tour.bo.common.exception.AuthorizationException;
import com.wemakeprice.tour.bo.domain.entity.auth.AccountMenu;
import com.wemakeprice.tour.bo.domain.entity.auth.AuthorityGroupMenu;
import com.wemakeprice.tour.bo.domain.entity.auth.Menu;
import com.wemakeprice.tour.bo.domain.service.auth.MenuService;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.MenuCDO;
import com.wemakeprice.tour.bo.domain.service.auth.dto.MenuRequestDTO;
import com.wemakeprice.tour.bo.domain.service.auth.dto.MenuUseFlagRequestDTO;
import com.wemakeprice.tour.bo.domain.service.auth.dto.NavigationBarDTO;
import com.wemakeprice.tour.bo.domain.service.auth.key.AccountMenuKey;
import com.wemakeprice.tour.bo.domain.service.auth.key.MenuKey;
import com.wemakeprice.tour.bo.domain.store.auth.AccountMenuStore;
import com.wemakeprice.tour.bo.domain.store.auth.AuthorityGroupMenuStore;
import com.wemakeprice.tour.bo.domain.store.auth.MenuStore;
import com.wemakeprice.tour.bo.service.converter.auth.MenuConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.wemakeprice.tour.bo.common.exception.AuthorizationException.ErrorMessage;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuServiceLogic implements MenuService {

    private final MenuStore menuStore;
    private final AccountMenuStore accountMenuStore;
    private final AuthorityGroupMenuStore authorityGroupMenuStore;

    private final MenuConverter menuConverter;

    @Override
    public long registerMenu(MenuCDO menuCDO) {
        return menuStore.insert(menuConverter.toDomain(menuCDO));
    }

    @Override
    public List<Menu> findMenusByCondition(MenuKey condition) {
        return menuStore.selectByCondition(condition);
    }

    @Override
    public int modifyMenuUseFlags(List<MenuUseFlagRequestDTO> menuUseFlagRequestDTOS) {
        return menuStore.updateUseFlags(menuUseFlagRequestDTOS);
    }

    @Override
    public boolean modifyMenu(Long menuId, MenuRequestDTO menuRequestDTO) {
        Menu menu = menuStore.selectById(menuId).orElseThrow(() ->
                new AuthorizationException(ErrorMessage.MENU_NOT_FOUND));
        menu.setMenuName(menuRequestDTO.getMenuName());
        menu.setLink(menuRequestDTO.getLink());
        return menuStore.update(menu);
    }

    @Override
    public boolean removeMenu(Long menuId) {
        Menu menu = menuStore.selectById(menuId).orElseThrow(() ->
                new AuthorizationException(ErrorMessage.MENU_NOT_FOUND));
        menu.setDeleteFlag(true);
        return menuStore.update(menu);
    }

    @Override
    @Cacheable("navigation-bars")
    public List<NavigationBarDTO> findNavigationBar(Long authorityGroupId, String accountId) {
        Set<Long> excludeMenuIdSet = new HashSet<>();
        Set<Long> parentMenuIdSet = new HashSet<>();
        List<NavigationBarDTO> navigationBarDTOS = new ArrayList<>();

        List<Menu> menus = menuStore.selectByCondition(MenuKey.builder().useFlag(true).build());
        Map<Long, AccountMenu> accountMenuMap = accountMenuStore.selectByCondition(
                AccountMenuKey.builder()
                        .accountId(accountId)
                        .build())
                .stream()
                .collect(Collectors.toMap(AccountMenu::getMenuId, Function.identity()));
        Map<Long, AuthorityGroupMenu> authorityGroupMenuMap =
                authorityGroupMenuStore.selectByAuthorityGroupId(authorityGroupId).stream()
                        .collect(Collectors.toMap(AuthorityGroupMenu::getMenuId, Function.identity()));

        for (Menu menu : menus) {
            retrieveMenuAuthority(accountMenuMap, authorityGroupMenuMap, menu).ifPresent(navigationBarDTO -> {
                if (!navigationBarDTO.isReadFlag()) {
                    excludeMenuIdSet.add(navigationBarDTO.getMenuId());
                    return;
                }
                if (!excludeMenuIdSet.contains(navigationBarDTO.getParentMenuId())
                        && (parentMenuIdSet.contains(navigationBarDTO.getParentMenuId())
                        || navigationBarDTO.getParentMenuId() == null)) {
                    parentMenuIdSet.add(navigationBarDTO.getMenuId());
                    navigationBarDTOS.add(navigationBarDTO);
                }
            });
        }
        return navigationBarDTOS;
    }

    private Optional<NavigationBarDTO> retrieveMenuAuthority(Map<Long, AccountMenu> accountMenuMap,
                                                             Map<Long, AuthorityGroupMenu> authorityGroupMenuMap,
                                                             Menu menu) {
        AccountMenu accountMenu = accountMenuMap.get(menu.getMenuId());
        if (accountMenu != null) {
            return Optional.ofNullable(menuConverter.toNavigationBarDTO(menu, accountMenu.isReadFlag(), accountMenu.isWriteFlag()));
        }
        AuthorityGroupMenu authorityGroupMenu = authorityGroupMenuMap.get(menu.getMenuId());
        if (authorityGroupMenu != null) {
            return Optional.ofNullable(menuConverter.toNavigationBarDTO(menu, authorityGroupMenu.isReadFlag(), authorityGroupMenu.isWriteFlag()));
        }
        return Optional.empty();
    }
}

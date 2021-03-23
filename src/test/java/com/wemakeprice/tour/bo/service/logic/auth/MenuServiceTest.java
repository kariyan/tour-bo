package com.wemakeprice.tour.bo.service.logic.auth;

import com.wemakeprice.tour.bo.common.request.ServiceContextHolder;
import com.wemakeprice.tour.bo.config.AuthorizationContextConfiguration;
import com.wemakeprice.tour.bo.config.WebClientConfig;
import com.wemakeprice.tour.bo.config.datasource.TourBoDataSourceConfig;
import com.wemakeprice.tour.bo.config.datasource.WtourDealDataSourceConfig;
import com.wemakeprice.tour.bo.config.kms.KmsConfig;
import com.wemakeprice.tour.bo.domain.entity.auth.Menu;
import com.wemakeprice.tour.bo.domain.service.auth.MenuService;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.MenuCDO;
import com.wemakeprice.tour.bo.domain.service.auth.dto.MenuRequestDTO;
import com.wemakeprice.tour.bo.domain.service.auth.dto.MenuUseFlagRequestDTO;
import com.wemakeprice.tour.bo.domain.service.auth.dto.NavigationBarDTO;
import com.wemakeprice.tour.bo.domain.service.auth.key.MenuKey;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Import({
        KmsConfig.class,
        WebClientConfig.class,
        TourBoDataSourceConfig.class,
        WtourDealDataSourceConfig.class,
})
@Transactional
@SpringBootTest(classes = AuthorizationContextConfiguration.class)
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @BeforeAll
    static void setUp() {
        ServiceContextHolder.ServiceContext context = ServiceContextHolder.getContext();
        context.setAccountId("2020070012");
        context.setAuthorityGroupId(1L);
    }

    @Test
    @DisplayName("메뉴 추가")
    void registerMenu() {
        MenuCDO menuCDO = new MenuCDO(7L, 1, "menu입력테스트", 1, "/category");

        assertThat(menuService.registerMenu(menuCDO)).isPositive();
    }

    @Test
    @DisplayName("메뉴 목록 조회")
    void findMenusByCondition() {
        MenuKey condition = MenuKey.builder().parentMenuId(7L).menuName("menu입력테스트").useFlag(false).build();
        List<Menu> menusByCondition = menuService.findMenusByCondition(condition);

        assertThat(menusByCondition.size()).isPositive();

        menusByCondition.forEach(menu -> log.info("menu : {}", menu.toString()));
    }

    @Test
    @DisplayName("메뉴 사용,미사용 처리")
    void modifyMenuUseFlags() {
        List<MenuUseFlagRequestDTO> menuUseFlagRequestDTOS = Collections.singletonList(MenuUseFlagRequestDTO.builder().menuId(16L).useFlag(false).build());

        assertThat(menuService.modifyMenuUseFlags(menuUseFlagRequestDTOS)).isEqualTo(1);

        Menu menu = menuService.findMenusByCondition(MenuKey.builder().parentMenuId(7L).menuName("menu입력테스트").useFlag(false).build()).get(0);
        assertThat(menu.isUseFlag()).isFalse();
        log.info("menu : {}", menu.toString());
    }

    @Test
    @DisplayName("메뉴 수정")
    void modifyMenu() {
        Long menuId = 16L;
        String menuName = "menu입력테스트Fix";
        String link = "linkFix";

        assertThat(menuService.modifyMenu(menuId, MenuRequestDTO.builder().menuName(menuName).link(link).build())).isTrue();

        List<Menu> menus = findMenuByCondition(7L, menuName, false);
        assertThat(menus).isNotNull();
        assertThat(menus.get(0).getMenuName()).isEqualTo(menuName);
        assertThat(menus.get(0).getLink()).isEqualTo(link);
    }

    @Test
    @DisplayName("메뉴 삭제")
    void removeMenu() {
        assertThat(menuService.removeMenu(16L)).isTrue();

        assertThat(findMenuByCondition(7L, "menu입력테스트", false)).isEmpty();
    }

    @Test
    @DisplayName("네비게이션바")
    void findNavigationBar() {
        ServiceContextHolder.ServiceContext context = ServiceContextHolder.getContext();
        List<NavigationBarDTO> navigationBar = menuService.findNavigationBar(context.getAuthorityGroupId(), context.getAccountId());
        assertThat(navigationBar).isNotNull();
        navigationBar.forEach(bar -> log.info("navigationBar : {}", bar.toString()));
    }

    List<Menu> findMenuByCondition(Long partnerMenuId, String menuName, boolean useFlag) {
        return menuService.findMenusByCondition(MenuKey.builder().parentMenuId(partnerMenuId).menuName(menuName).useFlag(useFlag).build());
    }
}

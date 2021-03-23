package com.wemakeprice.tour.bo.service.logic.auth;

import com.wemakeprice.tour.bo.common.request.ServiceContextHolder;
import com.wemakeprice.tour.bo.common.sharedtype.OffsetList;
import com.wemakeprice.tour.bo.config.AuthorizationContextConfiguration;
import com.wemakeprice.tour.bo.config.WebClientConfig;
import com.wemakeprice.tour.bo.config.aop.AuditableAspect;
import com.wemakeprice.tour.bo.config.datasource.TourBoDataSourceConfig;
import com.wemakeprice.tour.bo.config.datasource.WtourDealDataSourceConfig;
import com.wemakeprice.tour.bo.config.kms.KmsConfig;
import com.wemakeprice.tour.bo.domain.entity.auth.AccountMenu;
import com.wemakeprice.tour.bo.domain.entity.auth.AuthorityGroup;
import com.wemakeprice.tour.bo.domain.entity.auth.enumtype.AuthorityType;
import com.wemakeprice.tour.bo.domain.service.auth.AccountAuthorityService;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.AccountAuthorityGroupCDO;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.AccountMenuCDO;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.AuthorityGroupCDO;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.AuthorityGroupMenuCDO;
import com.wemakeprice.tour.bo.domain.service.auth.dto.AccountDTO;
import com.wemakeprice.tour.bo.domain.service.auth.dto.MenuAuthorityDTO;
import com.wemakeprice.tour.bo.domain.service.auth.key.AccountKey;
import com.wemakeprice.tour.bo.domain.service.auth.key.AccountMenuKey;
import com.wemakeprice.tour.bo.domain.store.auth.AccountMenuStore;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@Import({
        KmsConfig.class,
        AuditableAspect.class,
        WebClientConfig.class,
        TourBoDataSourceConfig.class,
        WtourDealDataSourceConfig.class,
})
@Transactional
@SpringBootTest(classes = AuthorizationContextConfiguration.class)
@AutoConfigureJson
@EnableAspectJAutoProxy
class AccountAuthorityServiceTest {

    @Autowired
    private AccountAuthorityService accountAuthorityService;

    @Autowired
    private AccountMenuStore accountMenuStore;

    @BeforeAll
    static void setUp() {
        ServiceContextHolder.getContext().setAccountId("2020070012");
    }

    @Test
    @DisplayName("권한 등급 생성")
    void registerAuthorityGroup() {
        AuthorityGroupCDO authorityGroupCDO = new AuthorityGroupCDO();
        authorityGroupCDO.setName("사용자");
        authorityGroupCDO.setAuthorityType(AuthorityType.USER);

        assertThat(accountAuthorityService.registerAuthorityGroup(authorityGroupCDO)).isPositive();
    }

    @Test
    @DisplayName("계정별 권한 설정 여부")
    void findAccountAuthorityExists() {
        assertThat(accountAuthorityService.findAccountAuthorityExists("2020070012")).isTrue();
    }

    @Test
    @DisplayName("권한 등급 목록")
    void findAuthorityGroups() {
        List<AuthorityGroup> authorityGroups = accountAuthorityService.findAuthorityGroups();
        assertThat(authorityGroups).isNotNull();

        authorityGroups.forEach(authorityGroup -> log.info("authorityGroup : {}", authorityGroup.toString()));
    }

    @Test
    @DisplayName("메뉴별 권한 설정 목록")
    void findMenuAuthoritiesByAuthorityGroupId() {
        List<MenuAuthorityDTO> menus = accountAuthorityService.findMenuAuthoritiesByAuthorityGroupId(1L);
        assertThat(menus).isNotNull();

        menus.forEach(menu -> log.info("menu : {}", menu.toString()));
    }

    @Test
    @DisplayName("계정별 권한 설정 목록")
    void findMenuAuthoritiesByAccountId() {
        List<MenuAuthorityDTO> menuAuthoritiesByAccountId = accountAuthorityService.findMenuAuthoritiesByAccountId("2020070012");
        assertThat(menuAuthoritiesByAccountId).isNotNull();

        menuAuthoritiesByAccountId.forEach(menuAuthorityDTO -> log.info("menuAuthorityDTO : {}", menuAuthorityDTO.toString()));
    }

    @Test
    @DisplayName("사용자 계정 조회")
    void findAccountById() {
        Optional<AccountDTO> accountById = accountAuthorityService.findAccountById("2020070012");
        assertThat(accountById).isPresent();

        accountById.ifPresent(account -> log.info(account.toString()));
    }

    @Test
    @DisplayName("사용자 권한 목록")
    void findAccountsByCondition() {
        AccountKey condition = new AccountKey();
        condition.setId("2020070012");

        OffsetList<AccountDTO> accountsByCondition = accountAuthorityService.findAccountsByCondition(condition, PageRequest.of(0, 10));
        assertThat(accountsByCondition).isNotNull();

        accountsByCondition.forEach(ac -> log.info("account : {}", ac.toString()));
    }

    @Test
    @DisplayName("메뉴별 권한 설정")
    void upsertAuthorityGroupMenus() {
        List<AuthorityGroupMenuCDO> authorityGroupMenuCDOS = Collections.singletonList(AuthorityGroupMenuCDO.builder().authorityGroupId(1L).menuId(7L).build());
        assertThat(accountAuthorityService.upsertAuthorityGroupMenus(authorityGroupMenuCDOS)).isPositive();
    }

    @Test
    @DisplayName("메뉴별 사용자 권한 설정")
    void upsertAccountMenus() {
        List<AccountMenuCDO> accountMenuCDOS = Collections.singletonList(AccountMenuCDO.builder().accountId("2020070012").menuId(16L).readFlag(true).build());
        assertThat(accountAuthorityService.upsertAccountMenus(accountMenuCDOS)).isPositive();

        List<AccountMenu> accountMenus = accountMenuStore.selectByCondition(AccountMenuKey.builder().accountId("2020070012").build());
        assertThat(accountMenus.stream().anyMatch(account ->
                account.getAccountId().equals("2020070012") && account.isReadFlag()))
                .isTrue();
    }

    @Test
    @DisplayName("계정 권한 설정")
    void upsertAccountAuthorityGroup() {
        AccountAuthorityGroupCDO accountAuthorityGroupCDO = new AccountAuthorityGroupCDO();
        accountAuthorityGroupCDO.setAccountId("2020070012");
        accountAuthorityGroupCDO.setAuthorityGroupId(1L);
        int result = accountAuthorityService.upsertAccountAuthorityGroup(accountAuthorityGroupCDO);

        assertThat(result).isPositive();

        AccountKey condition = new AccountKey();
        condition.setId("2020070012");
        OffsetList<AccountDTO> accountDTOS = accountAuthorityService.findAccountsByCondition(condition, PageRequest.of(0, 10));
        assertThat(accountDTOS.getResults().stream().anyMatch(account ->
                account.getAccountId().equals("2020070012") && account.getAuthorityGroupId().equals(1L))).isTrue();
    }

    @Test
    @DisplayName("계정 권한 삭제")
    void deleteAccountMenusByAccountId() {
        assertThat(accountAuthorityService.deleteAccountMenusByAccountId("2020070012")).isPositive();
        assertThat(accountMenuStore.selectByCondition(AccountMenuKey.builder().accountId("2020070012").build()).size()).isZero();
    }
}

package com.wemakeprice.tour.bo.service.controller.auth;

import com.wemakeprice.tour.bo.common.annotation.ApiPageable;
import com.wemakeprice.tour.bo.common.request.URIMapping;
import com.wemakeprice.tour.bo.common.sharedtype.OffsetList;
import com.wemakeprice.tour.bo.domain.entity.auth.AuthorityGroup;
import com.wemakeprice.tour.bo.domain.service.auth.AccountAuthorityService;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.AccountAuthorityGroupCDO;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.AccountMenuCDO;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.AuthorityGroupCDO;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.AuthorityGroupMenuCDO;
import com.wemakeprice.tour.bo.domain.service.auth.dto.AccountDTO;
import com.wemakeprice.tour.bo.domain.service.auth.dto.MenuAuthorityDTO;
import com.wemakeprice.tour.bo.domain.service.auth.key.AccountKey;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(URIMapping.V1_BASE_URI + "/auth/accounts")
@RequiredArgsConstructor
public class AccountAuthorityRestController {
    private final AccountAuthorityService accountAuthorityService;

    @Operation(summary = "사용자 권한 목록", description = "계정관리 - 권한 목록")
    @GetMapping
    @ApiPageable
    public OffsetList<AccountDTO> findAccountsByCondition(AccountKey condition, Pageable pageable) {
        return accountAuthorityService.findAccountsByCondition(condition, pageable);
    }

    @Operation(summary = "권한 등급 목록", description = "계정관리 - 권한관리 팝업")
    @GetMapping("/authority-groups")
    public List<AuthorityGroup> findAuthorityGroups() {
        return accountAuthorityService.findAuthorityGroups();
    }

    @Operation(summary = "권한 등급 생성", description = "계정관리 - 권한관리 팝업")
    @PostMapping("/authority-groups")
    public int registerAuthorityGroup(@RequestBody AuthorityGroupCDO authorityGroupCDO) {
        return accountAuthorityService.registerAuthorityGroup(authorityGroupCDO);
    }

    @Operation(summary = "메뉴별 권한 설정 목록", description = "계정관리 - 권한관리 팝업")
    @GetMapping("/authority-group-menus/{authorityGroupId:[\\d]+}")
    public List<MenuAuthorityDTO> findAuthorityGroupMenus(@PathVariable Long authorityGroupId) {
        return accountAuthorityService.findMenuAuthoritiesByAuthorityGroupId(authorityGroupId);
    }

    @Operation(summary = "메뉴별 권한 설정", description = "계정관리 - 권한관리 팝업")
    @PutMapping("/authority-group-menus")
    public int upsertAuthorityGroupMenus(@RequestBody List<AuthorityGroupMenuCDO> authorityGroupMenuCDOS) {
        return accountAuthorityService.upsertAuthorityGroupMenus(authorityGroupMenuCDOS);
    }

    @Operation(summary = "사용자 계정 조회", description = "계정관리 - 계정권한관리 팝업")
    @GetMapping("/{accountId}")
    public Optional<AccountDTO> findAccountById(@PathVariable String accountId) {
        return accountAuthorityService.findAccountById(accountId);
    }

    @Operation(summary = "계정 권한 설정", description = "계정관리 - 계정권한관리 팝업")
    @PutMapping("/account-authority-groups")
    public int registerAccountMenus(@RequestBody AccountAuthorityGroupCDO accountAuthorityGroupCDO) {
        return accountAuthorityService.upsertAccountAuthorityGroup(accountAuthorityGroupCDO);
    }

    @Operation(summary = "계정별 권한 설정 여부", description = "계정관리 - 계정권한관리 팝업")
    @GetMapping("/account-menus/{accountId}/exists")
    public boolean findAccountMenusExists(@PathVariable String accountId) {
        return accountAuthorityService.findAccountAuthorityExists(accountId);
    }

    @Operation(summary = "계정별 권한 설정 목록", description = "계정관리 - 계정권한관리 팝업")
    @GetMapping("/account-menus/{accountId}")
    public List<MenuAuthorityDTO> findAccountMenusByAccountId(@PathVariable String accountId) {
        return accountAuthorityService.findMenuAuthoritiesByAccountId(accountId);
    }

    @Operation(summary = "메뉴별 사용자 권한 설정", description = "계정관리 - 계정권한관리 팝업")
    @PutMapping("/account-menus")
    public int upsertAccountMenus(@RequestBody List<AccountMenuCDO> accountMenuCDOS) {
        return accountAuthorityService.upsertAccountMenus(accountMenuCDOS);
    }

    @Operation(summary = "계정 권한 삭제", description = "계정관리 - 계정권한관리 팝업")
    @DeleteMapping("/account-menus/{accountId}")
    public int deleteAccountMenus(@PathVariable String accountId) {
        return accountAuthorityService.deleteAccountMenusByAccountId(accountId);
    }
}

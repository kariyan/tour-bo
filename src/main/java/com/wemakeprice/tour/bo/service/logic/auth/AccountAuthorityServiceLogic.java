package com.wemakeprice.tour.bo.service.logic.auth;

import com.wemakeprice.tour.bo.common.sharedtype.OffsetList;
import com.wemakeprice.tour.bo.domain.entity.auth.*;
import com.wemakeprice.tour.bo.domain.service.auth.AccountAuthorityService;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.AccountAuthorityGroupCDO;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.AccountMenuCDO;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.AuthorityGroupCDO;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.AuthorityGroupMenuCDO;
import com.wemakeprice.tour.bo.domain.service.auth.dto.AccountDTO;
import com.wemakeprice.tour.bo.domain.service.auth.dto.MenuAuthorityDTO;
import com.wemakeprice.tour.bo.domain.service.auth.key.AccountKey;
import com.wemakeprice.tour.bo.domain.service.auth.key.AccountMenuKey;
import com.wemakeprice.tour.bo.domain.service.auth.key.MenuKey;
import com.wemakeprice.tour.bo.domain.store.auth.*;
import com.wemakeprice.tour.bo.service.converter.auth.AccountAuthorityGroupConverter;
import com.wemakeprice.tour.bo.service.converter.auth.AccountMenuConverter;
import com.wemakeprice.tour.bo.service.converter.auth.AuthorityGroupConverter;
import com.wemakeprice.tour.bo.service.converter.auth.AuthorityGroupMenuConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountAuthorityServiceLogic implements AccountAuthorityService {

    private final MenuStore menuStore;
    private final AccountStore accountStore;
    private final AccountMenuStore accountMenuStore;
    private final AuthorityGroupStore authorityGroupStore;
    private final AuthorityGroupMenuStore authorityGroupMenuStore;
    private final AccountAuthorityGroupStore accountAuthorityGroupStore;

    private final AccountMenuConverter accountMenuConverter;
    private final AuthorityGroupConverter authorityGroupConverter;
    private final AuthorityGroupMenuConverter authorityGroupMenuConverter;
    private final AccountAuthorityGroupConverter accountAuthorityGroupConverter;

    @Override
    public int registerAuthorityGroup(AuthorityGroupCDO authorityGroupCDO) {
        return authorityGroupStore.insert(authorityGroupConverter.toDomain(authorityGroupCDO));
    }

    @Override
    public boolean findAccountAuthorityExists(@NotBlank String accountId) {
        return accountMenuStore.countByAccountId(accountId) > 0;
    }

    @Override
    public List<AuthorityGroup> findAuthorityGroups() {
        return authorityGroupStore.selectAll();
    }

    @Override
    public List<MenuAuthorityDTO> findMenuAuthoritiesByAuthorityGroupId(Long authorityGroupId) {
        List<Menu> menus = menuStore.selectByCondition(new MenuKey());
        Map<Long, AuthorityGroupMenu> authorityGroupMenuMap = authorityGroupMenuStore.selectByAuthorityGroupId(authorityGroupId)
                .stream()
                .collect(Collectors.toMap(AuthorityGroupMenu::getMenuId, Function.identity()));

        return menus.stream().map(menu -> {
            AuthorityGroupMenu authorityGroupMenu = authorityGroupMenuMap.get(menu.getMenuId());
            return MenuAuthorityDTO.builder()
                    .menuId(menu.getMenuId())
                    .parentMenuId(menu.getParentMenuId())
                    .menuLevel(menu.getMenuLevel())
                    .menuName(menu.getMenuName())
                    .sortNo(menu.getSortNo())
                    .readFlag(authorityGroupMenu != null && authorityGroupMenu.isReadFlag())
                    .writeFlag(authorityGroupMenu != null && authorityGroupMenu.isWriteFlag())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public List<MenuAuthorityDTO> findMenuAuthoritiesByAccountId(String accountId) {
        List<Menu> menus = menuStore.selectByCondition(new MenuKey());

        Map<Long, AccountMenu> accountMenuMap = accountMenuStore.selectByCondition(AccountMenuKey.builder().accountId(accountId).build())
                .stream()
                .collect(Collectors.toMap(AccountMenu::getMenuId, Function.identity()));

        return menus.stream().map(menu -> {
            AccountMenu accountMenu = accountMenuMap.get(menu.getMenuId());
            return MenuAuthorityDTO.builder()
                    .menuId(menu.getMenuId())
                    .parentMenuId(menu.getParentMenuId())
                    .menuLevel(menu.getMenuLevel())
                    .menuName(menu.getMenuName())
                    .sortNo(menu.getSortNo())
                    .readFlag(accountMenu != null && accountMenu.isReadFlag())
                    .writeFlag(accountMenu != null && accountMenu.isWriteFlag())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public Optional<AccountDTO> findAccountById(String accountId) {
        Map<String, AccountAuthorityGroup> accountAuthorityGroupMap =
                retrieveAccountAuthorityGroupMap(Collections.singletonList(accountId));
        Map<Long, AuthorityGroup> authorityGroupMap = retrieveAuthorityGroupMap();

        Optional<AccountDTO> optionalAccountDTO = accountStore.selectById(accountId);
        if (optionalAccountDTO.isPresent()) {
            AccountDTO accountDTO = optionalAccountDTO.get();
            AccountAuthorityGroup accountAuthorityGroup = accountAuthorityGroupMap.get(accountId);
            if (accountAuthorityGroup != null) {
                accountDTO.setAuthorityGroupId(accountAuthorityGroup.getAuthorityGroupId());
                accountDTO.setAuthorityGroupName(authorityGroupMap.get(accountAuthorityGroup.getAuthorityGroupId()).getName());
                accountDTO.setAuthorityType(authorityGroupMap.get(accountAuthorityGroup.getAuthorityGroupId()).getAuthorityType());
            }
        }

        return optionalAccountDTO;
    }

    @Override
    public OffsetList<AccountDTO> findAccountsByCondition(AccountKey condition, Pageable pageable) {
        OffsetList<AccountDTO> accountDTOS = accountStore.selectByCondition(condition, pageable);
        List<String> accountIds = accountDTOS.getResults().stream()
                .map(AccountDTO::getAccountId)
                .collect(Collectors.toList());

        if (!ObjectUtils.isEmpty(accountIds)) {
            Map<String, AccountAuthorityGroup> accountAuthorityGroupMap = retrieveAccountAuthorityGroupMap(accountIds);
            Map<Long, AuthorityGroup> authorityGroupMap = retrieveAuthorityGroupMap();

            for (AccountDTO accountDTO : accountDTOS) {
                AccountAuthorityGroup accountAuthorityGroup = accountAuthorityGroupMap.get(accountDTO.getAccountId());
                if (accountAuthorityGroup != null) {
                    Long authorityGroupId = accountAuthorityGroup.getAuthorityGroupId();
                    String authorityGroupName = authorityGroupMap.get(authorityGroupId).getName();

                    accountDTO.setAuthorityGroupId(authorityGroupId);
                    accountDTO.setAuthorityGroupName(authorityGroupName);
                }
            }
        }

        return accountDTOS;
    }

    private Map<Long, AuthorityGroup> retrieveAuthorityGroupMap() {
        return authorityGroupStore.selectAll()
                .stream()
                .collect(Collectors.toMap(AuthorityGroup::getId, Function.identity()));
    }

    private Map<String, AccountAuthorityGroup> retrieveAccountAuthorityGroupMap(List<String> accountIds) {
        return accountAuthorityGroupStore.selectAllByAccountIds(accountIds).stream()
                .collect(Collectors.toMap(AccountAuthorityGroup::getAccountId, Function.identity()));
    }

    @Override
    public int upsertAuthorityGroupMenus(List<AuthorityGroupMenuCDO> authorityGroupMenuCDOS) {
        Set<Long> authorityGroupIds = authorityGroupMenuCDOS.stream()
                .map(AuthorityGroupMenuCDO::getAuthorityGroupId)
                .collect(Collectors.toSet());
        if (!ObjectUtils.isEmpty(authorityGroupIds)) {
            authorityGroupMenuStore.deleteAllByAuthorityGroupIds(authorityGroupIds);
        }
        return authorityGroupMenuStore.insertAll(authorityGroupMenuConverter.toDomain(authorityGroupMenuCDOS));
    }

    @Override
    public int upsertAccountMenus(List<AccountMenuCDO> accountMenuCDOS) {
        Set<String> accountIds = accountMenuCDOS
                .stream()
                .map(AccountMenuCDO::getAccountId)
                .collect(Collectors.toSet());
        if (!ObjectUtils.isEmpty(accountIds)) {
            accountMenuStore.deleteAllByAccountIds(accountIds);
        }
        return accountMenuStore.insertAll(accountMenuConverter.toDomain(accountMenuCDOS));
    }

    @Override
    public int upsertAccountAuthorityGroup(@Valid AccountAuthorityGroupCDO accountAuthorityGroupCDO) {
        if (ObjectUtils.isEmpty(accountAuthorityGroupCDO.getAuthorityGroupId())) {
            return accountAuthorityGroupStore.delete(accountAuthorityGroupCDO.getAccountId());
        }

        if (accountAuthorityGroupStore.selectByAccountId(accountAuthorityGroupCDO.getAccountId()).isPresent()) {
            return accountAuthorityGroupStore.update(accountAuthorityGroupConverter.toDomain(accountAuthorityGroupCDO));
        } else {
            return accountAuthorityGroupStore.insert(accountAuthorityGroupConverter.toDomain(accountAuthorityGroupCDO));
        }
    }

    @Override
    public int deleteAccountMenusByAccountId(@NotBlank String accountId) {
        return accountMenuStore.deleteAllByAccountIds(Collections.singletonList(accountId));
    }
}

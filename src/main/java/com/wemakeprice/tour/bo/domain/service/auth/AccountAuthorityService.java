package com.wemakeprice.tour.bo.domain.service.auth;

import com.wemakeprice.tour.bo.common.sharedtype.OffsetList;
import com.wemakeprice.tour.bo.domain.entity.auth.AuthorityGroup;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.AccountAuthorityGroupCDO;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.AccountMenuCDO;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.AuthorityGroupCDO;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.AuthorityGroupMenuCDO;
import com.wemakeprice.tour.bo.domain.service.auth.dto.AccountDTO;
import com.wemakeprice.tour.bo.domain.service.auth.dto.MenuAuthorityDTO;
import com.wemakeprice.tour.bo.domain.service.auth.key.AccountKey;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Validated
public interface AccountAuthorityService {

    int registerAuthorityGroup(@Valid AuthorityGroupCDO authorityGroupCDO);

    boolean findAccountAuthorityExists(@NotBlank String accountId);

    List<AuthorityGroup> findAuthorityGroups();

    List<MenuAuthorityDTO> findMenuAuthoritiesByAuthorityGroupId(@NotNull Long authorityGroupId);

    List<MenuAuthorityDTO> findMenuAuthoritiesByAccountId(@NotBlank String accountId);

    Optional<AccountDTO> findAccountById(@NotBlank String accountId);

    OffsetList<AccountDTO> findAccountsByCondition(@NotNull AccountKey condition, @NotNull Pageable pageable);

    int upsertAuthorityGroupMenus(@NotEmpty List<@Valid AuthorityGroupMenuCDO> authorityGroupMenuCDOS);

    int upsertAccountMenus(@NotEmpty List<@Valid AccountMenuCDO> accountMenuCDOS);

    int upsertAccountAuthorityGroup(@Valid AccountAuthorityGroupCDO accountAuthorityGroupCDO);

    int deleteAccountMenusByAccountId(@NotBlank String accountId);
}

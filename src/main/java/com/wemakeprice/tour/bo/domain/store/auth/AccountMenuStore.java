package com.wemakeprice.tour.bo.domain.store.auth;

import com.wemakeprice.tour.bo.domain.entity.auth.AccountMenu;
import com.wemakeprice.tour.bo.domain.service.auth.key.AccountMenuKey;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.List;

@Validated
public interface AccountMenuStore {

    int insertAll(@NotEmpty List<AccountMenu> accountMenus);

    int countByAccountId(@NotBlank String accountId);

    List<AccountMenu> selectByCondition(AccountMenuKey condition);

    int deleteAllByAccountIds(@NotEmpty Collection<String> accountIds);
}

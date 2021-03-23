package com.wemakeprice.tour.bo.service.store.auth;

import com.wemakeprice.tour.bo.domain.entity.auth.AccountMenu;
import com.wemakeprice.tour.bo.domain.service.auth.key.AccountMenuKey;
import com.wemakeprice.tour.bo.domain.store.auth.AccountMenuStore;
import com.wemakeprice.tour.bo.service.store.auth.mapper.AccountMenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AccountMenuMyBatisStore implements AccountMenuStore {

    private final AccountMenuMapper accountMenuMapper;

    @Override
    public int insertAll(List<AccountMenu> accountMenus) {
        return accountMenuMapper.insertAll(accountMenus);
    }

    @Override
    public int countByAccountId(String accountId) {
        return accountMenuMapper.countByAccountId(accountId);
    }

    @Override
    public List<AccountMenu> selectByCondition(AccountMenuKey condition) {
        return accountMenuMapper.selectByCondition(condition);
    }

    @Override
    public int deleteAllByAccountIds(Collection<String> accountIds) {
        return accountMenuMapper.deleteAllByAccountIds(accountIds);
    }
}

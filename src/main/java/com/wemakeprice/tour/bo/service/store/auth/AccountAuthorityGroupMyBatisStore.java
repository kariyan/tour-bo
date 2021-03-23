package com.wemakeprice.tour.bo.service.store.auth;

import com.wemakeprice.tour.bo.domain.entity.auth.AccountAuthorityGroup;
import com.wemakeprice.tour.bo.domain.store.auth.AccountAuthorityGroupStore;
import com.wemakeprice.tour.bo.service.store.auth.mapper.AccountAuthorityGroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountAuthorityGroupMyBatisStore implements AccountAuthorityGroupStore {

    private final AccountAuthorityGroupMapper accountAuthorityGroupMapper;

    @Override
    public int insert(AccountAuthorityGroup accountAuthorityGroup) {
        return accountAuthorityGroupMapper.insert(accountAuthorityGroup);
    }

    @Override
    public Optional<AccountAuthorityGroup> selectByAccountId(String accountId) {
        return Optional.ofNullable(accountAuthorityGroupMapper.selectByAccountId(accountId));
    }

    @Override
    public List<AccountAuthorityGroup> selectAllByAccountIds(Collection<String> accountIds) {
        return accountAuthorityGroupMapper.selectAllByAccountIds(accountIds);
    }

    @Override
    public int update(AccountAuthorityGroup accountAuthorityGroup) {
        return accountAuthorityGroupMapper.update(accountAuthorityGroup);
    }

    @Override
    public int delete(String accountId) {
        return accountAuthorityGroupMapper.delete(accountId);
    }
}

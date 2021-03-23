package com.wemakeprice.tour.bo.service.store.auth;

import com.wemakeprice.tour.bo.common.sharedtype.OffsetList;
import com.wemakeprice.tour.bo.domain.service.auth.dto.AccountDTO;
import com.wemakeprice.tour.bo.domain.service.auth.key.AccountKey;
import com.wemakeprice.tour.bo.domain.store.auth.AccountStore;
import com.wemakeprice.tour.bo.service.store.auth.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountMyBatisStore implements AccountStore {

    private final AccountMapper accountMapper;

    @Override
    public Optional<AccountDTO> selectById(String accountId) {
        return Optional.ofNullable(accountMapper.selectById(accountId));
    }

    @Override
    public OffsetList<AccountDTO> selectByCondition(AccountKey condition, Pageable pageable) {
        List<AccountDTO> results = accountMapper.selectByCondition(condition, pageable);
        int count = accountMapper.countByCondition(condition);
        return new OffsetList<>(results, count);
    }
}

package com.wemakeprice.tour.bo.service.store.auth.mapper;

import com.wemakeprice.tour.bo.common.mapper.TourBoMapper;
import com.wemakeprice.tour.bo.domain.entity.auth.AccountAuthorityGroup;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@TourBoMapper
public interface AccountAuthorityGroupMapper {

    int insert(AccountAuthorityGroup accountAuthorityGroup);

    AccountAuthorityGroup selectByAccountId(String accountId);

    List<AccountAuthorityGroup> selectAllByAccountIds(@Param("accountIds") Collection<String> accountIds);

    int update(AccountAuthorityGroup accountAuthorityGroup);

    int delete(String accountId);
}

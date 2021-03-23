package com.wemakeprice.tour.bo.service.store.auth.mapper;

import com.wemakeprice.tour.bo.common.mapper.TourBoMapper;
import com.wemakeprice.tour.bo.domain.entity.auth.AccountMenu;
import com.wemakeprice.tour.bo.domain.service.auth.key.AccountMenuKey;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@TourBoMapper
public interface AccountMenuMapper {

    int insertAll(List<AccountMenu> accountMenus);

    int countByAccountId(String accountId);

    List<AccountMenu> selectByCondition(@Param("condition") AccountMenuKey condition);

    int deleteAllByAccountIds(Collection<String> accountIds);
}

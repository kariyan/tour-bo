package com.wemakeprice.tour.bo.service.store.auth.mapper;

import com.wemakeprice.tour.bo.common.mapper.WtourDealMapper;
import com.wemakeprice.tour.bo.domain.service.auth.dto.AccountDTO;
import com.wemakeprice.tour.bo.domain.service.auth.key.AccountKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

@WtourDealMapper
public interface AccountMapper {

    AccountDTO selectById(String accountId);

    List<AccountDTO> selectByCondition(@Param("condition") AccountKey condition, @Param("pageable") Pageable pageable);

    int countByCondition(@Param("condition") AccountKey condition);
}

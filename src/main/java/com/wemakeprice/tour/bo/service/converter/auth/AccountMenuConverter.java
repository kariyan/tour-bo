package com.wemakeprice.tour.bo.service.converter.auth;

import com.wemakeprice.tour.bo.config.ConverterConfig;
import com.wemakeprice.tour.bo.domain.entity.auth.AccountMenu;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.AccountMenuCDO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = ConverterConfig.class)
public interface AccountMenuConverter {

    List<AccountMenu> toDomain(List<AccountMenuCDO> accountMenuCDOS);
}

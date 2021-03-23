package com.wemakeprice.tour.bo.service.converter.auth;

import com.wemakeprice.tour.bo.config.ConverterConfig;
import com.wemakeprice.tour.bo.domain.entity.auth.AccountAuthorityGroup;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.AccountAuthorityGroupCDO;
import org.mapstruct.Mapper;

@Mapper(config = ConverterConfig.class)
public interface AccountAuthorityGroupConverter {

    AccountAuthorityGroup toDomain(AccountAuthorityGroupCDO accountAuthorityGroupCDO);
}

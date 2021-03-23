package com.wemakeprice.tour.bo.service.converter.auth;

import com.wemakeprice.tour.bo.config.ConverterConfig;
import com.wemakeprice.tour.bo.domain.entity.auth.AuthorityGroup;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.AuthorityGroupCDO;
import org.mapstruct.Mapper;

@Mapper(config = ConverterConfig.class)
public interface AuthorityGroupConverter {

    AuthorityGroup toDomain(AuthorityGroupCDO authorityGroupCDO);
}

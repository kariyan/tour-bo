package com.wemakeprice.tour.bo.service.converter.auth;

import com.wemakeprice.tour.bo.config.ConverterConfig;
import com.wemakeprice.tour.bo.domain.entity.auth.AuthorityGroupMenu;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.AuthorityGroupMenuCDO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = ConverterConfig.class)
public interface AuthorityGroupMenuConverter {

    List<AuthorityGroupMenu> toDomain(List<AuthorityGroupMenuCDO> authorityGroupMenuCDOS);
}

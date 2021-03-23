package com.wemakeprice.tour.bo.service.converter.auth;

import com.wemakeprice.tour.bo.config.ConverterConfig;
import com.wemakeprice.tour.bo.domain.entity.auth.Menu;
import com.wemakeprice.tour.bo.domain.service.auth.cdo.MenuCDO;
import com.wemakeprice.tour.bo.domain.service.auth.dto.NavigationBarDTO;
import org.mapstruct.Mapper;

@Mapper(config = ConverterConfig.class)
public interface MenuConverter {

    Menu toDomain(MenuCDO menuCDO);

    NavigationBarDTO toNavigationBarDTO(Menu menu, boolean readFlag, boolean writeFlag);
}

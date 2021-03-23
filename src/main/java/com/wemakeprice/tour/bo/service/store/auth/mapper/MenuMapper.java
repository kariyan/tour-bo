package com.wemakeprice.tour.bo.service.store.auth.mapper;

import com.wemakeprice.tour.bo.common.mapper.TourBoMapper;
import com.wemakeprice.tour.bo.domain.entity.auth.Menu;
import com.wemakeprice.tour.bo.domain.service.auth.key.MenuKey;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@TourBoMapper
public interface MenuMapper {

    void insert(Menu menu);

    Menu select(Long menuId);

    List<Menu> selectByCondition(@Param("condition") MenuKey condition);

    int update(Menu menu);

    int updateUseFlags(@Param("menuIds") Collection<Long> menuIds, @Param("useFlag") Boolean useFlag, @Param("updater") String updater);
}

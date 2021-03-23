package com.wemakeprice.tour.bo.service.store.auth.mapper;

import com.wemakeprice.tour.bo.common.mapper.TourBoMapper;
import com.wemakeprice.tour.bo.domain.entity.auth.AuthorityGroup;

import java.util.List;

@TourBoMapper
public interface AuthorityGroupMapper {

    List<AuthorityGroup> selectAll();

    int insert(AuthorityGroup authorityGroup);
}

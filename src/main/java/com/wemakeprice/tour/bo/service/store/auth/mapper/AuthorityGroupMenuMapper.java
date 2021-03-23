package com.wemakeprice.tour.bo.service.store.auth.mapper;

import com.wemakeprice.tour.bo.common.mapper.TourBoMapper;
import com.wemakeprice.tour.bo.domain.entity.auth.AuthorityGroupMenu;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@TourBoMapper
public interface AuthorityGroupMenuMapper {

    List<AuthorityGroupMenu> selectByAuthorityGroupId(Long authorityGroupId);

    int insertAll(@Param("authorityGroupMenus") List<AuthorityGroupMenu> authorityGroupMenus);

    int deleteAllByAuthorityGroupIds(Collection<Long> authorityGroupIds);
}

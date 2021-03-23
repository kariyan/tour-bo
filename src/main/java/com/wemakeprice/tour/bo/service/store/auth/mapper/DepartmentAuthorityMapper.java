package com.wemakeprice.tour.bo.service.store.auth.mapper;

import com.wemakeprice.tour.bo.common.mapper.TourBoMapper;
import com.wemakeprice.tour.bo.domain.entity.auth.DepartmentAuthority;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@TourBoMapper
public interface DepartmentAuthorityMapper {

    int insertAll(@Param("departmentCds") List<String> departmentCds, @Param("accountId") String accountId);

    List<DepartmentAuthority> selectAll();

    int deleteAllByDepartmentCds(@Param("departmentCds") List<String> departmentCds);
}

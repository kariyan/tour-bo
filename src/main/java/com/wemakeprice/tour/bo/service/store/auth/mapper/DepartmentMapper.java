package com.wemakeprice.tour.bo.service.store.auth.mapper;

import com.wemakeprice.tour.bo.common.mapper.WtourDealMapper;
import com.wemakeprice.tour.bo.domain.service.auth.dto.DepartmentDTO;

import java.util.List;

@WtourDealMapper
public interface DepartmentMapper {

    List<DepartmentDTO> selectAll();
}

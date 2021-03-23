package com.wemakeprice.tour.bo.domain.store.auth;

import com.wemakeprice.tour.bo.domain.service.auth.dto.DepartmentDTO;

import java.util.List;

public interface DepartmentStore {

    List<DepartmentDTO> selectAll();
}

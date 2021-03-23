package com.wemakeprice.tour.bo.service.store.auth;

import com.wemakeprice.tour.bo.domain.service.auth.dto.DepartmentDTO;
import com.wemakeprice.tour.bo.domain.store.auth.DepartmentStore;
import com.wemakeprice.tour.bo.service.store.auth.mapper.DepartmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DepartmentMyBatisStore implements DepartmentStore {

    private final DepartmentMapper departmentMapper;

    @Override
    public List<DepartmentDTO> selectAll() {
        return departmentMapper.selectAll();
    }
}

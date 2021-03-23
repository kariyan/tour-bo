package com.wemakeprice.tour.bo.service.store.auth;

import com.wemakeprice.tour.bo.common.request.ServiceContextHolder;
import com.wemakeprice.tour.bo.domain.entity.auth.DepartmentAuthority;
import com.wemakeprice.tour.bo.domain.store.auth.DepartmentAuthorityStore;
import com.wemakeprice.tour.bo.service.store.auth.mapper.DepartmentAuthorityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DepartmentAuthorityMyBatisStore implements DepartmentAuthorityStore {

    private final DepartmentAuthorityMapper departmentAuthorityMapper;

    public int insertAll(List<String> departmentCds) {
        ServiceContextHolder.ServiceContext context = ServiceContextHolder.getContext();
        return departmentAuthorityMapper.insertAll(departmentCds, context.getAccountId());
    }

    public List<DepartmentAuthority> selectAll() {
        return departmentAuthorityMapper.selectAll();
    }

    public int deleteAllByDepartmentCds(List<String> departmentCds) {
        return departmentAuthorityMapper.deleteAllByDepartmentCds(departmentCds);
    }
}

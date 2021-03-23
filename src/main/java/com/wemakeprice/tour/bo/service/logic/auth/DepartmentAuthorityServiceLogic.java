package com.wemakeprice.tour.bo.service.logic.auth;

import com.wemakeprice.tour.bo.domain.entity.auth.DepartmentAuthority;
import com.wemakeprice.tour.bo.domain.service.auth.DepartmentAuthorityService;
import com.wemakeprice.tour.bo.domain.service.auth.dto.DepartmentDTO;
import com.wemakeprice.tour.bo.service.store.auth.DepartmentAuthorityMyBatisStore;
import com.wemakeprice.tour.bo.service.store.auth.DepartmentMyBatisStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartmentAuthorityServiceLogic implements DepartmentAuthorityService {

    private final DepartmentMyBatisStore departmentStore;
    private final DepartmentAuthorityMyBatisStore departmentAuthorityStore;

    @Override
    public int registerDepartmentAuthorities(List<String> departmentCds) {
        return departmentAuthorityStore.insertAll(departmentCds);
    }

    @Override
    public List<DepartmentDTO> findAllDepartmentDTOS() {
        List<DepartmentDTO> departmentDTOS = departmentStore.selectAll();
        Map<String, DepartmentDTO> departmentDTOMap = departmentDTOS.stream()
                .collect(Collectors.toMap(DepartmentDTO::getDepartmentCd, Function.identity(), (a, b) -> a));
        for (DepartmentAuthority departmentAuthority : departmentAuthorityStore.selectAll()) {
            DepartmentDTO departmentDTO = departmentDTOMap.get(departmentAuthority.getDepartmentCd());
            departmentDTO.setAccessFlag(true);
        }
        return departmentDTOS;
    }

    @Override
    public int removeDepartmentAuthorities(List<String> departmentCds) {
        return departmentAuthorityStore.deleteAllByDepartmentCds(departmentCds);
    }
}

package com.wemakeprice.tour.bo.service.controller.auth;

import com.wemakeprice.tour.bo.common.request.URIMapping;
import com.wemakeprice.tour.bo.domain.service.auth.DepartmentAuthorityService;
import com.wemakeprice.tour.bo.domain.service.auth.dto.DepartmentDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(URIMapping.V1_BASE_URI + "/auth/departments/authorities")
@RestController
@RequiredArgsConstructor
public class DepartmentAuthorityRestController {
    private final DepartmentAuthorityService departmentAuthorityService;

    @PostMapping
    public int registerDepartmentAuthorities(@RequestBody DepartmentCdDTOS departmentCdDTOS) {
        return departmentAuthorityService.registerDepartmentAuthorities(departmentCdDTOS.getDepartmentCds());
    }

    @GetMapping
    public List<DepartmentDTO> findAllDepartmentDTOS() {
        return departmentAuthorityService.findAllDepartmentDTOS();
    }

    @DeleteMapping
    public int removeDepartmentAuthorities(@RequestParam("departmentCds") List<String> departmentCds) {
        return departmentAuthorityService.removeDepartmentAuthorities(departmentCds);
    }

    @Getter
    @Setter
    private static class DepartmentCdDTOS {
        private List<String> departmentCds;
    }
}

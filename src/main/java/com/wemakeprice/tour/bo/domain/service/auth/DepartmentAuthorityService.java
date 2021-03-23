package com.wemakeprice.tour.bo.domain.service.auth;

import com.wemakeprice.tour.bo.domain.service.auth.dto.DepartmentDTO;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Validated
public interface DepartmentAuthorityService {

    int registerDepartmentAuthorities(@NotEmpty List<@NotBlank String> departmentCds);

    List<DepartmentDTO> findAllDepartmentDTOS();

    int removeDepartmentAuthorities(@NotEmpty List<@NotBlank String> departmentCds);
}

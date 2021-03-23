package com.wemakeprice.tour.bo.domain.service.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DepartmentDTO {
    @Schema(description = "부서코드")
    private String departmentCd;
    @Schema(description = "상위부서코드")
    private String parentDepartmentCd;
    @Schema(description = "부서명")
    private String departmentName;
    @Schema(description = "부서 depth")
    private String departmentLevel;
    @Schema(description = "부서 정렬 순서")
    private String departmentOrder;
    @Schema(description = "접근 권한 여부")
    private boolean accessFlag;
}

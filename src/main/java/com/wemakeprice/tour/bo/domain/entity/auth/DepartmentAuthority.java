package com.wemakeprice.tour.bo.domain.entity.auth;

import com.wemakeprice.tour.bo.common.entity.AuditLog;
import com.wemakeprice.tour.bo.common.entity.Auditable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DepartmentAuthority implements Auditable {
    private String departmentCd;
    private final AuditLog auditLog = new AuditLog();
}

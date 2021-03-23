package com.wemakeprice.tour.bo.domain.entity.auth;

import com.wemakeprice.tour.bo.common.entity.AuditLog;
import com.wemakeprice.tour.bo.common.entity.Auditable;
import com.wemakeprice.tour.bo.domain.entity.auth.enumtype.AuthorityType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AuthorityGroup implements Auditable {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private AuthorityType authorityType;
    private Long defaultMenuId;
    private boolean deleteFlag;
    private final AuditLog auditLog = new AuditLog();
}

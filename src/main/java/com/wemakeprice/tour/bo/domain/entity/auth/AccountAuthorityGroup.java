package com.wemakeprice.tour.bo.domain.entity.auth;

import com.wemakeprice.tour.bo.common.entity.AuditLog;
import com.wemakeprice.tour.bo.common.entity.Auditable;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AccountAuthorityGroup implements Auditable {
    @NotNull
    private Long authorityGroupId;
    @NotBlank
    private String accountId;
    private final AuditLog auditLog = new AuditLog();
}

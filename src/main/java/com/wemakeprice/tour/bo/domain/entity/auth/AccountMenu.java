package com.wemakeprice.tour.bo.domain.entity.auth;

import com.wemakeprice.tour.bo.common.entity.AuditLog;
import com.wemakeprice.tour.bo.common.entity.Auditable;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AccountMenu implements Auditable {
    @NotNull
    private Long menuId;
    @NotBlank
    private String accountId;
    private boolean readFlag;
    private boolean writeFlag;
    private final AuditLog auditLog = new AuditLog();
}

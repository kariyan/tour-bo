package com.wemakeprice.tour.bo.domain.entity.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wemakeprice.tour.bo.common.entity.AuditLog;
import com.wemakeprice.tour.bo.common.entity.Auditable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccLog implements Auditable {
    private Long logId;
    private String logIp;
    private String requestType;
    private String logUri;
    private final AuditLog auditLog = new AuditLog();
}

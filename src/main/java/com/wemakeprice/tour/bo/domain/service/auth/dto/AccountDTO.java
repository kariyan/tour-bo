package com.wemakeprice.tour.bo.domain.service.auth.dto;

import com.wemakeprice.tour.bo.common.annotation.Secured;
import com.wemakeprice.tour.bo.common.annotation.SecuredBean;
import com.wemakeprice.tour.bo.common.entity.AuditLog;
import com.wemakeprice.tour.bo.common.entity.Auditable;
import com.wemakeprice.tour.bo.common.enumtype.PrivacyGrade;
import com.wemakeprice.tour.bo.domain.entity.auth.enumtype.AccountStatus;
import com.wemakeprice.tour.bo.domain.entity.auth.enumtype.AuthorityType;
import lombok.*;

@Getter
@Builder
@ToString
@SecuredBean
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountDTO implements Auditable {
    private String accountId;
    private String accountName;
    @Setter
    private Long authorityGroupId;
    @Setter
    private String authorityGroupName;
    @Setter
    private AuthorityType authorityType;
    @Secured(PrivacyGrade.P8)
    private String accountEmail;
    @Secured(PrivacyGrade.P7)
    private String phone;
    private String employmentCd;
    private String positionCd;
    private String jobCd;
    private String departmentCd;
    private String departmentName;
    private AccountStatus accountStatus;
    private boolean useFlag;
    private int passwdFailCnt;
    private final AuditLog auditLog = new AuditLog();
}

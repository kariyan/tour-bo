package com.wemakeprice.tour.bo.common.sharedtype;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.AssertTrue;

@Getter
@Setter
public class JwtAccountInfoDTO {
    @Schema(hidden = true)
    private String accountId;
    private String accountName;
    private String domainType;
    private String accountEmail;
    private String positionCd;
    private String jobCd;
    private String jobName;
    private String onesDutyCd;
    private String departmentCd;
    private String departmentName;

    @AssertTrue(message = "계정 권한이 유효하지 않습니다. 다시 로그인을 해주세요.")
    public boolean isValid() {
        if (isNull(accountId)) {
            return false;
        }
        return !isNull(accountName);
    }

    private boolean isNull(String value) {
        return StringUtils.isBlank(value) || "null".equals(value);
    }
}

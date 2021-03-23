package com.wemakeprice.tour.bo.domain.service.auth.cdo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AccountAuthorityGroupCDO {
    private Long authorityGroupId;
    @NotBlank
    private String accountId;
}

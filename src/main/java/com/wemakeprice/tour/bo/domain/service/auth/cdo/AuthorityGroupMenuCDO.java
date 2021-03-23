package com.wemakeprice.tour.bo.domain.service.auth.cdo;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorityGroupMenuCDO {
    @NotNull
    private Long authorityGroupId;
    @NotNull
    private Long menuId;
    private boolean readFlag;
    private boolean writeFlag;
}

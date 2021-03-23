package com.wemakeprice.tour.bo.domain.service.auth.cdo;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountMenuCDO {
    @NotEmpty
    private String accountId;
    @NotNull
    private Long menuId;
    private boolean readFlag;
    private boolean writeFlag;
}

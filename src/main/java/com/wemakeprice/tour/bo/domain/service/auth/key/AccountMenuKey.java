package com.wemakeprice.tour.bo.domain.service.auth.key;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountMenuKey {
    private Long menuId;
    private String accountId;
}

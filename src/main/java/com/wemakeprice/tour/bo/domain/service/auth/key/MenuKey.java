package com.wemakeprice.tour.bo.domain.service.auth.key;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuKey {
    private Long parentMenuId;
    private String menuName;
    private Boolean useFlag;
}

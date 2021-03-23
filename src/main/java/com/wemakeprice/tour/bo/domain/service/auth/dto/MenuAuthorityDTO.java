package com.wemakeprice.tour.bo.domain.service.auth.dto;

import lombok.*;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuAuthorityDTO {
    private Long menuId;
    private Long parentMenuId;
    private int menuLevel;
    private String menuName;
    private int sortNo;
    private boolean readFlag;
    private boolean writeFlag;
}

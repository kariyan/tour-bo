package com.wemakeprice.tour.bo.domain.service.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NavigationBarDTO {
    @Schema(description = "메뉴 ID")
    private Long menuId;
    @Schema(description = "부모 메뉴 ID")
    private Long parentMenuId;
    @Schema(description = "메뉴 LEVEL")
    private int menuLevel;
    @Schema(description = "정렬 순서")
    private int sortNo;
    @Schema(description = "메뉴명")
    private String menuName;
    @Schema(description = "링크")
    private String link;
    @Schema(description = "보기권한 여부")
    private boolean readFlag;
    @Schema(description = "등록/수정권한 여부")
    private boolean writeFlag;
}

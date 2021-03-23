package com.wemakeprice.tour.bo.domain.entity.auth;

import com.wemakeprice.tour.bo.common.entity.AuditLog;
import com.wemakeprice.tour.bo.common.entity.Auditable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@ToString
public class Menu implements Auditable {
    @NotNull
    private Long menuId;
    private Long parentMenuId;
    @PositiveOrZero
    private int menuLevel;
    @NotBlank
    private String menuName;
    @PositiveOrZero
    private int sortNo;
    private String link;
    private boolean useFlag;
    private boolean deleteFlag;
    private final AuditLog auditLog = new AuditLog();
}

package com.wemakeprice.tour.bo.domain.service.auth.cdo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Getter
@AllArgsConstructor
public class MenuCDO {
    private final Long parentMenuId;
    @Max(99)
    @Positive
    private final int menuLevel;
    @Length(max = 50)
    @NotBlank
    private final String menuName;
    @Max(99)
    @PositiveOrZero
    private final int sortNo;
    @Length(max = 2000)
    @NotBlank
    private final String link;
}

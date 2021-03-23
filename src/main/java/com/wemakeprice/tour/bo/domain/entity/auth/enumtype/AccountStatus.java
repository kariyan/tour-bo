package com.wemakeprice.tour.bo.domain.entity.auth.enumtype;

import com.wemakeprice.tour.bo.common.entity.TitledEnum;
import lombok.Getter;

@Getter
public enum AccountStatus implements TitledEnum {
    WORK("재직중"),
    LAYOFF("휴직"),
    RETIREMENT("퇴직");

    private final String title;

    AccountStatus(String title) {
        this.title = title;
    }
}

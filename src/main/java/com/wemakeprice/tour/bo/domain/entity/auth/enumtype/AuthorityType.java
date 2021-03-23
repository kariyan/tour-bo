package com.wemakeprice.tour.bo.domain.entity.auth.enumtype;

import com.wemakeprice.tour.bo.common.entity.TitledEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthorityType implements TitledEnum {
    ADMIN("관리자"),
    USER("사용자");

    private final String title;
}

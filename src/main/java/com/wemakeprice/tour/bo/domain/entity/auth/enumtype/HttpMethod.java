package com.wemakeprice.tour.bo.domain.entity.auth.enumtype;

import com.wemakeprice.tour.bo.common.entity.TitledEnum;

public enum HttpMethod implements TitledEnum {
    GET,
    PUT,
    POST,
    DELETE,
    OPTIONS;

    @Override
    public String getTitle() {
        return this.name();
    }
}

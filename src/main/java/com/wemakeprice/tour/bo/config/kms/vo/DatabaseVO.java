package com.wemakeprice.tour.bo.config.kms.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabaseVO {
    private DbUserVO cluster;
    private DbUserVO reader;
}

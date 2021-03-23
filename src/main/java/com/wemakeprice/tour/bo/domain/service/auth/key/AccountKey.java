package com.wemakeprice.tour.bo.domain.service.auth.key;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountKey {
    private String id;
    private String name;
    private Boolean useFlag;
}

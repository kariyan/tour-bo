package com.wemakeprice.tour.bo.config.kms.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AwsCredentialVO {
    private String accessKey;
    private String secretKey;
}

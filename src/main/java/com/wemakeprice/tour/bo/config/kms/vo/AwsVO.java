package com.wemakeprice.tour.bo.config.kms.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AwsVO {
    private AwsCredentialVO s3;
    private AwsCredentialVO sqs;
}

package com.wemakeprice.tour.bo.config.kms;

import com.wemakeprice.tour.bo.config.kms.vo.AwsVO;
import com.wemakeprice.tour.bo.config.kms.vo.DatabaseVO;
import com.wemakeprice.tour.bo.config.kms.vo.RedisVO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KmsProperties {
    private final DatabaseVO database;
    private final RedisVO redis;
    private final AwsVO aws;
}

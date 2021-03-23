package com.wemakeprice.tour.bo.config.kms.dto;

import com.wemakeprice.tour.bo.config.kms.vo.AwsVO;
import com.wemakeprice.tour.bo.config.kms.vo.DatabaseVO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class KmsDTO {
    private DatabaseVO database;
    private AwsVO aws;
}

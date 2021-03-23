package com.wemakeprice.tour.bo.config.kms.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wemakeprice.tour.bo.common.util.AES256;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EncryptKmsDTO {
    @JsonProperty(value = "P1")
    private AES256.EncryptVO p1;
    @JsonProperty(value = "P2")
    private AES256.EncryptVO p2;
    @JsonProperty(value = "P3")
    private AES256.EncryptVO p3;
    @JsonProperty(value = "P4")
    private AES256.EncryptVO p4;
    @JsonProperty(value = "P5")
    private AES256.EncryptVO p5;
    @JsonProperty(value = "P6")
    private AES256.EncryptVO p6;
    @JsonProperty(value = "P7")
    private AES256.EncryptVO p7;
    @JsonProperty(value = "P8")
    private AES256.EncryptVO p8;
    @JsonProperty(value = "P90")
    private AES256.EncryptVO p90;
    @JsonProperty(value = "P91")
    private AES256.EncryptVO p91;
}

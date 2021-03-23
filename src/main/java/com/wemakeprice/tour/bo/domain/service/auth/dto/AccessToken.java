package com.wemakeprice.tour.bo.domain.service.auth.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessToken {
    private String token;
}

package com.wemakeprice.tour.bo.domain.service.auth.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuUseFlagRequestDTO {
    @NotNull
    private Long menuId;
    @NotNull
    private Boolean useFlag;
}

package com.wemakeprice.tour.bo.domain.service.auth.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuRequestDTO {
    @NotBlank
    private String menuName;
    @NotBlank
    private String link;
}

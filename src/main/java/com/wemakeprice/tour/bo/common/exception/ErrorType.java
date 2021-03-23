package com.wemakeprice.tour.bo.common.exception;

import com.wemakeprice.tour.bo.common.entity.TitledEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType implements TitledEnum {

    HTTP("HTTP 에러"),
    NO_LOGIN("로그인 에러"),
    UNDEFINED("서버 에러"),
    ILLEGAL_ARGUMENT("유효성 검증 에러"),
    APPLICATION_ERROR("비지니스 에러"),

    AUTHENTICATION_ERROR("인증 오류"),
    AUTHORITY_ERROR("권한관리 오류"),
    EXHIBITION_ERROR("전시관리 오류"),
    ;

    private final String title;
}

package com.wemakeprice.tour.bo.common.exception;

import com.wemakeprice.tour.bo.common.entity.TitledEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;

public class AuthenticationException extends ApplicationException {

    private static final long serialVersionUID = 3282990736722768353L;

    public AuthenticationException(ErrorMessage message, Object... args) {
        super(MessageFormat.format(message.getTitle(), args));
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErrorMessage implements TitledEnum {
        JWT_EXPIRED("장시간 미사용으로 인하여 로그아웃 처리되었습니다."),
        NO_ACCOUNT_FOUND("사용자 계정이 존재하지 않습니다. 다시 로그인을 해주세요."),
        JWT_VALIDATION_ERROR("사용자 정보가 유효하지 않습니다. 다시 로그인을 해주세요."),
        ;

        private final String title;
    }
}

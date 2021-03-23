package com.wemakeprice.tour.bo.common.exception;

import com.wemakeprice.tour.bo.common.entity.TitledEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;

public class AuthorizationException extends ApplicationException {

    private static final long serialVersionUID = 4323179883967928422L;

    public AuthorizationException(ErrorMessage message, Object... args) {
        super(MessageFormat.format(message.getTitle(), args));
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErrorMessage implements TitledEnum {
        MENU_NOT_FOUND("메뉴를 찾을 수 없습니다."),
        UNAUTHORIZED("권한이 없습니다."),
        ;

        private final String title;
    }
}

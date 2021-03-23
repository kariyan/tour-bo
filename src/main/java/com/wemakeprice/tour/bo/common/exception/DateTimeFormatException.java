package com.wemakeprice.tour.bo.common.exception;

import com.wemakeprice.tour.bo.common.entity.TitledEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;

public class DateTimeFormatException extends ApplicationException{

    public DateTimeFormatException(ErrorMessage message, Object... args) {
        super(MessageFormat.format(message.getTitle(), args));
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErrorMessage implements TitledEnum {
        START_DATE_TIME_BEFORE_END_DATE_TIME("적용 시작일이 종료일 보다 이후일 수 없습니다."),
        ;

        private final String title;
    }
}

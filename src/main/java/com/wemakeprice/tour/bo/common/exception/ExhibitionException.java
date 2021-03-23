package com.wemakeprice.tour.bo.common.exception;

import com.wemakeprice.tour.bo.common.entity.TitledEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;

public class ExhibitionException extends ApplicationException {

    private static final long serialVersionUID = -6547814249415123581L;

    public ExhibitionException(ErrorMessage message, Object... args) {
        super(MessageFormat.format(message.getTitle(), args));
    }

    @Getter
    @RequiredArgsConstructor
    public enum ErrorMessage implements TitledEnum {
        DATE_TYPE_IS_NOT_EXISTS("검색 기간과 검색구분이 함께 조회 조건에 추가되어야 합니다."),
        CATEGORY_TOTAL_CNT_EXCEED("중복 기간에 같은 적용 타겟으로 사용중인 카테고리 총 개수가 초과됩니다.({0}Depth 메뉴 최대 {1}까지)"),
        DUPLICATED_PRIORITY("중복 기간에 같은 적용 타겟으로 사용중인 카테고리 중 중복된 우선순위가 존재합니다."),
        CANNOT_MODIFIABLE("삭제/수정 권한이 없는 카테고리 입니다."),
        CANNOT_ADD_PC_OPTION("PC 옵션 값은 입력할 수 없습니다."),
        CANNOT_ADD_MOBILE_OPTION("Mobile 옵션 값은 입력할 수 없습니다."),
        NO_CATEGORY("id : {0}인 카테고리가 존재하지 않습니다."),
        NO_PARENT_CATEGORY("상위 카테고리가 없습니다."),
        PRIORITY_NO_EXCEED("{0}depth의 최대 우선순위 No는 {1}입니다."),
        NUMBER_CANNOT_LESS_THAN_ONE("0보다 커야 합니다.");
        private final String title;
    }
}

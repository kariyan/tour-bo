package com.wemakeprice.tour.bo.common.util;

import com.wemakeprice.tour.bo.common.entity.CodeEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.EnumSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeEnumUtils {
    public static <T extends Enum<T> & CodeEnum> T getCodeEnum(Class<T> enumClass, String code) {
        return EnumSet.allOf(enumClass).stream()
                .filter(type -> type.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}

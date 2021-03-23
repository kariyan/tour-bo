package com.wemakeprice.tour.bo.common.annotation;

import com.wemakeprice.tour.bo.common.enumtype.PrivacyGrade;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Secured {
    PrivacyGrade value();
}

package com.wemakeprice.tour.bo.common.mapper;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marker interface for Tour Main MyBatis mappers
 */
@Inherited
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface TourMainMapper {
}

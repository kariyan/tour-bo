package com.wemakeprice.tour.bo.common.annotation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(name = "page", in = ParameterIn.QUERY, example = "0", description = "Results page you want to retrieve (0..N)")
@Parameter(name = "size", in = ParameterIn.QUERY, example = "20", description = "Number of records per page.")
@Parameter(name = "sort", in = ParameterIn.QUERY,
        example = "CREATED,DESC(최신등록순) / NAME(카테고리명) / ID(카테고리ID) / START_DATE,DESC(적용시작순) / END_DATE,DESC(적용종료순)",
        description = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. " +
                "Multiple sort criteria are supported.")
public @interface ApiPageable {
}

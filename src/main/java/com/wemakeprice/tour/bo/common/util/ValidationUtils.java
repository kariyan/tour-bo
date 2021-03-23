package com.wemakeprice.tour.bo.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtils {
    private static final ValidatorFactory VALIDATOR_FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Pattern EMAIL_PATTERN = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])");

    public static boolean isValidEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return false;
        }

        Matcher m = EMAIL_PATTERN.matcher(email);
        return m.matches();
    }

    public static <T> void validate(T target) {
        validate(target, true);
    }

    public static <T> void validate(T target, boolean showFieldName) {
        Validator validator = VALIDATOR_FACTORY.getValidator();
        validator.validate(target).stream().findFirst().ifPresent(violation -> {
            if (showFieldName) {
                throw new ValidationException(String.format("%s : %s", violation.getPropertyPath().toString(),
                        violation.getMessage()));
            } else {
                throw new ValidationException(violation.getMessage());
            }
        });
    }

    public static <T> void validate(T target, Class<?> groups, boolean showFieldName) {
        Validator validator = VALIDATOR_FACTORY.getValidator();
        validator.validate(target, groups).stream().findFirst().ifPresent(violation -> {
            if (showFieldName) {
                throw new ValidationException(String.format("%s : %s", violation.getPropertyPath().toString(),
                        violation.getMessage()));
            } else {
                throw new ValidationException(violation.getMessage());
            }
        });
    }
}

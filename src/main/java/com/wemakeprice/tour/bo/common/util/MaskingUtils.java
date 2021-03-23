package com.wemakeprice.tour.bo.common.util;

import com.wemakeprice.tour.bo.common.enumtype.PrivacyGrade;
import com.wemakeprice.tour.bo.common.exception.MaskingException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MaskingUtils {

    private static final Pattern PHONE_NUMBER_MID_3_PATTERN = Pattern.compile("^([0-9*]{2,3})-?([0-9*]{3})-?([0-9*]{4})$");
    private static final Pattern PHONE_NUMBER_MID_4_PATTERN = Pattern.compile("^([0-9*]{2,3})-?([0-9*]{4})-?([0-9*]{4})$");
    private static final Pattern ENGLISH_NAME_PATTERN = Pattern.compile("[a-zA-Z ]+");

    public static String masking(String str, PrivacyGrade privacyGrade) throws MaskingException {

        if (StringUtils.isEmpty(str) || privacyGrade == null) {
            throw new MaskingException("처리 대상 및 처리 방식은 필수값입니다.");
        }
        if (str.contains("*")) { // 이미 마스킹 처리됨
            return str;
        }

        switch (privacyGrade) {
            case P1:
                return maskingBankAccount(str);
            case P7:
                return maskingPhoneNumber(str);
            case P8:
                return maskingEmail(str);
            case P90:
                return maskingName(str);
            default:
                throw new MaskingException("지원하지 않는 마스킹 방식입니다.");
        }
    }

    private static String maskingPhoneNumber(String str) {

        Matcher matcherMid3 = PHONE_NUMBER_MID_3_PATTERN.matcher(str);
        Matcher matcherMid4 = PHONE_NUMBER_MID_4_PATTERN.matcher(str);
        if (matcherMid3.find()) {
            return matcherMid3.replaceAll("$1-***-$3"); // 치환
        }
        if (matcherMid4.find()) {
            return matcherMid4.replaceAll("$1-****-$3"); // 치환
        }
        return "***";
    }

    private static String maskingName(String name) {

        if (ENGLISH_NAME_PATTERN.matcher(name).matches()) { // english
            if (name.length() > 8) {
                return maskingString(name, 4, name.length() - 4);
            } else {
                return maskingString(name, 2, name.length());
            }

        } else {
            if (name.length() <= 3) {
                return maskingString(name, 1, 2);
            } else {
                return maskingString(name, 1, name.length() - 1);
            }
        }
    }

    private static String maskingString(String target, int fromIn, int toIn) {

        int stringBeforeLength = target.length();
        if (stringBeforeLength == 1) {
            return "*";
        }
        int starNum = toIn - fromIn;
        StringBuilder star = new StringBuilder();
        for (int i = 0; i < starNum; i++) {
            star.append("*");
        }
        return target.substring(0, fromIn) + star + target.substring(toIn, stringBeforeLength);
    }

    private static String maskingEmail(String email) throws MaskingException {

        if (!ValidationUtils.isValidEmail(email)) {
            throw new MaskingException("이메일 주소가 올바르지 않습니다.");
        }

        String emailMask;
        String[] emailSplit = email.split("@");
        String emailFront = emailSplit[0];

        int emailFrontLength = emailFront.length();
        StringBuilder star = new StringBuilder();

        if (emailFrontLength == 1) {
            emailMask = "";
            star.append("*");
            emailMask = emailMask + star + "@" + emailSplit[1];

        } else if (emailFrontLength == 2) {
            emailMask = emailFront.substring(0, 1);
            star.append("*");
            emailMask = emailMask + star + "@" + emailSplit[1];

        } else if (emailFrontLength == 3) {
            emailMask = emailFront.substring(0, 2);
            star.append("*");
            emailMask = emailMask + star + "@" + emailSplit[1];

        } else {
            emailMask = emailFront.substring(0, 3);
            for (int i = 0; i < emailFrontLength - 3; i++) {
                star.append("*");
            }
            emailMask = emailMask + star + "@" + emailSplit[1];
        }

        return emailMask;
    }

    private static String maskingBankAccount(String account) {

        return maskingString(account, 3, 7);
    }
}

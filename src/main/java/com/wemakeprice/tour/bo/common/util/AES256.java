package com.wemakeprice.tour.bo.common.util;

import com.wemakeprice.tour.bo.common.enumtype.PrivacyGrade;
import com.wemakeprice.tour.bo.common.exception.DecodingException;
import com.wemakeprice.tour.bo.common.exception.EncodingException;
import com.wemakeprice.tour.bo.config.kms.dto.EncryptKmsDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;

@Slf4j
public class AES256 {

    private static AES256 INSTANCE;

    @Setter
    private Map<PrivacyGrade, EncryptVO> encryptKeyMap;

    public static synchronized AES256 getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new AES256();
        }
        return INSTANCE;
    }

    public String encode(String value, PrivacyGrade privacyGrade) throws EncodingException {

        if (StringUtils.isEmpty(value) || privacyGrade == null) {
            throw new EncodingException("처리 대상 및 처리 방식은 필수값입니다.");
        }
        return encodeWithAES(value, privacyGrade, false);
    }

    public String encodeUrlSafe(String value, PrivacyGrade privacyGrade) throws EncodingException {

        if (StringUtils.isEmpty(value) || privacyGrade == null) {
            throw new EncodingException("처리 대상 및 처리 방식은 필수값입니다.");
        }
        return encodeWithAES(value, privacyGrade, true);
    }

    private String encodeWithAES(String value, PrivacyGrade privacyGrade, boolean urlSafe) throws EncodingException {

        EncryptVO encryptVO = encryptKeyMap.get(privacyGrade);

        try {
            byte[] keyData = encryptVO.getKey().getBytes();
            SecretKey secureKey = new SecretKeySpec(keyData, "AES");

            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(encryptVO.getIv().getBytes()));

            byte[] encrypted = c.doFinal(value.getBytes(StandardCharsets.UTF_8));
            return urlSafe ? new String(Base64.encodeBase64URLSafe(encrypted)) :
                    new String(Base64.encodeBase64(encrypted));

        } catch (Exception e) {
            throw new EncodingException("암호화 시 오류가 발생하였습니다.", e);
        }
    }

    public String decode(String value, PrivacyGrade privacyGrade) throws DecodingException {

        if (StringUtils.isEmpty(value) || privacyGrade == null) {
            throw new DecodingException("처리 대상 및 처리 방식은 필수값입니다.");
        }

        EncryptVO encryptVO = encryptKeyMap.get(privacyGrade);
        try {
            byte[] keyData = encryptVO.getKey().getBytes();
            SecretKey secureKey = new SecretKeySpec(keyData, "AES");

            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(encryptVO.getIv().getBytes(StandardCharsets.UTF_8)));

            byte[] byteStr = Base64.decodeBase64(value.getBytes());
            return new String(c.doFinal(byteStr), StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new DecodingException("복호화 시 오류가 발생하였습니다.", e);
        }
    }

    public void initialize(EncryptKmsDTO encryptKmsDTO) {
        encryptKeyMap = new EnumMap<>(PrivacyGrade.class);
        encryptKeyMap.put(PrivacyGrade.P1, encryptKmsDTO.getP1());
        encryptKeyMap.put(PrivacyGrade.P2, encryptKmsDTO.getP2());
        encryptKeyMap.put(PrivacyGrade.P3, encryptKmsDTO.getP3());
        encryptKeyMap.put(PrivacyGrade.P4, encryptKmsDTO.getP4());
        encryptKeyMap.put(PrivacyGrade.P5, encryptKmsDTO.getP5());
        encryptKeyMap.put(PrivacyGrade.P6, encryptKmsDTO.getP6());
        encryptKeyMap.put(PrivacyGrade.P7, encryptKmsDTO.getP7());
        encryptKeyMap.put(PrivacyGrade.P8, encryptKmsDTO.getP8());
        encryptKeyMap.put(PrivacyGrade.P90, encryptKmsDTO.getP90());
        encryptKeyMap.put(PrivacyGrade.P91, encryptKmsDTO.getP91());
    }

    @Getter
    @Setter
    public static class EncryptVO {
        private String key;
        private String iv;
    }
}

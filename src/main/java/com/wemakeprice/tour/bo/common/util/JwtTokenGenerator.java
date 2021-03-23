package com.wemakeprice.tour.bo.common.util;

import com.wemakeprice.tour.bo.common.exception.AuthenticationException;
import com.wemakeprice.tour.bo.common.sharedtype.JwtAccountInfoDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

import static com.wemakeprice.tour.bo.common.exception.AuthenticationException.ErrorMessage.JWT_EXPIRED;
import static com.wemakeprice.tour.bo.common.exception.AuthenticationException.ErrorMessage.JWT_VALIDATION_ERROR;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class JwtTokenGenerator {

    private static byte[] generateKey(String secret) {
        return secret.getBytes(StandardCharsets.UTF_8);
    }

    public static boolean isValid(String jwt, String secret) {
        try {
            Jwts.parser().setSigningKey(generateKey(secret)).parseClaimsJws(jwt);
            return true;

        } catch (ExpiredJwtException ex) {
            throw new AuthenticationException(JWT_EXPIRED);

        } catch (Exception e) {
            throw new AuthenticationException(JWT_VALIDATION_ERROR);
        }
    }

    public static JwtAccountInfoDTO get(String secret) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String jwt = request.getHeader("Authorization").replace("Bearer ", "");
        return getJwtAccountInfoDto(secret, jwt);
    }

    static JwtAccountInfoDTO getJwtAccountInfoDto(String secret, String jwt) {
        Jws<Claims> claims;
        JwtAccountInfoDTO jwtAccountInfoDTO = new JwtAccountInfoDTO();

        try {
            claims = Jwts.parser().setSigningKey(secret.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(jwt);
            jwtAccountInfoDTO.setAccountId(String.valueOf(claims.getBody().get("account_id")));
            jwtAccountInfoDTO.setAccountName(String.valueOf(claims.getBody().get("account_name")));
            jwtAccountInfoDTO.setDomainType(String.valueOf(claims.getBody().get("domain_type")));
            jwtAccountInfoDTO.setAccountEmail(String.valueOf(claims.getBody().get("account_email")));
            jwtAccountInfoDTO.setDepartmentCd(String.valueOf(claims.getBody().get("department_cd")));
            jwtAccountInfoDTO.setDepartmentName(String.valueOf(claims.getBody().get("department_name")));
            jwtAccountInfoDTO.setJobCd(String.valueOf(claims.getBody().get("job_cd")));
            jwtAccountInfoDTO.setJobName(String.valueOf(claims.getBody().get("job_name")));
            jwtAccountInfoDTO.setOnesDutyCd(String.valueOf(claims.getBody().get("ones_duty_cd")));
            jwtAccountInfoDTO.setPositionCd(String.valueOf(claims.getBody().get("position_cd")));
            return jwtAccountInfoDTO;

        } catch (Exception e) {
            throw new AuthenticationException(JWT_VALIDATION_ERROR);
        }
    }
}

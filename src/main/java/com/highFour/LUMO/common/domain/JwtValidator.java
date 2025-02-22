package com.highFour.LUMO.common.domain;

import com.highFour.LUMO.common.exception.BaseCustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.highFour.LUMO.common.exceptionType.TokenExceptionType.*;

@RequiredArgsConstructor
@Component
public class JwtValidator {
    private final SecretKeyFactory secretKeyFactory;

    public String validateToken(String token) {
        try {
            getClaims(token);
            return "VALID_JWT";
        } catch(Exception e) {
            return "INVALID_JWT";
        }
    }

    public long getMemberIdFromToken(String token) {
        Claims claims;
        try {
            claims = getClaims(token);
        } catch(Exception e) {
            throw new BaseCustomException(INVALID_TOKEN);
        }
        return Long.parseLong(claims.get("memberId").toString());
    }

    // 정상적인 토큰인지 판단하는 메서드
    public boolean isValidToken(String token) {
        return StringUtils.hasText(token) && validateToken(token).equals("VALID_JWT");
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKeyFactory.createSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

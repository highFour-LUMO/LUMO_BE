package com.highFour.LUMO.common.securities;

import com.highFour.LUMO.common.domain.SecretKeyFactory;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final SecretKeyFactory secretKeyFactory;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.secret_key}")
    private String secretKey;

    @Value("${jwt.token.access_expiration_time}")
    private Long accessExpirationTime;

    @Value("${jwt.token.refresh_expiration_time}")
    private Long refreshExpirationTime;

    public String createToken(Long memberId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        Date now = new Date();

        return Jwts.builder()
                .issuedAt(now)
                .expiration(new Date(now.getTime() + accessExpirationTime))
                .subject(Long.toString(memberId))
                .claims(claims)
                .signWith(secretKeyFactory.createSecretKey())
                .compact();
    }

    public String createRefreshToken(Long memberId, String role) {
        Date now = new Date();
        String refreshToken = Jwts.builder()
                .subject(String.valueOf(memberId))
                .issuedAt(now)
                .expiration(new Date(now.getTime() + refreshExpirationTime))
                .signWith(secretKeyFactory.createSecretKey())
                .claim("role", role)
                .compact();

        return refreshToken;
    }
}
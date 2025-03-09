package com.highFour.LUMO.member.service;



import com.highFour.LUMO.common.exceptionType.MemberExceptionType;
import com.highFour.LUMO.common.exceptionType.TokenExceptionType;
import com.highFour.LUMO.member.dto.MemberSignUpReq;
import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.jwt.service.JwtService;
import com.highFour.LUMO.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import jakarta.servlet.http.HttpServletRequest;

import java.time.Duration;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RedisTemplate<String, Object> redisTemplate;

    public void signUp(MemberSignUpReq memberSignUpReq) {
        if (memberRepository.findByEmail(memberSignUpReq.email()).isPresent()) {
            throw new ResponseStatusException(MemberExceptionType.NOT_A_NEW_MEMBER.httpStatus(), MemberExceptionType.NOT_A_NEW_MEMBER.message());
        }

        if (memberRepository.findByNickname(memberSignUpReq.nickname()).isPresent()) {
            throw new ResponseStatusException(MemberExceptionType.NOT_A_NEW_NICKNAME.httpStatus(), MemberExceptionType.NOT_A_NEW_NICKNAME.message());
        }

        Member member = memberSignUpReq.toEntity();
        member.passwordEncode(passwordEncoder);
        memberRepository.save(member);
    }

    public void logout(HttpServletRequest request) {
        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new ResponseStatusException(TokenExceptionType.INVALID_TOKEN.httpStatus(), TokenExceptionType.INVALID_TOKEN.message()));

        if (!jwtService.isTokenValid(accessToken)) {
            throw new ResponseStatusException(TokenExceptionType.INVALID_TOKEN.httpStatus(), TokenExceptionType.INVALID_TOKEN.message());
        }

        String email = jwtService.extractEmail(accessToken)
                .orElseThrow(() -> new ResponseStatusException(TokenExceptionType.MEMBER_NOT_FOUND.httpStatus(), TokenExceptionType.MEMBER_NOT_FOUND.message()));

        // Access Token을 블랙리스트에 저장 (만료시간까지)
        long expiration = jwtService.getAccessTokenExpiration(accessToken);
        if (expiration > 0) {
            redisTemplate.opsForValue().set("blacklist:" + accessToken, "logout", Duration.ofMillis(expiration));
            log.info("✅ Access Token 블랙리스트 추가 완료 (만료 시간: {}ms)", expiration);
        } else {
            log.warn("🚨 Access Token이 이미 만료됨 - 블랙리스트에 저장하지 않음");
        }

        // Refresh Token 삭제
        if (jwtService.getRefreshTokenFromRedis(email) != null) {
            jwtService.deleteRefreshToken(email);
            log.info("✅ Refresh Token 삭제 완료: {}", email);
        } else {
            log.warn("🚨 Refresh Token이 이미 삭제되었거나 없음: {}", email);
        }

        log.info("✅ 로그아웃 성공 - 사용자 이메일: {}", email);
    }


    public void deleteMember(Long id, HttpServletRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(MemberExceptionType.MEMBER_NOT_FOUND.httpStatus(),
                        MemberExceptionType.MEMBER_NOT_FOUND.message()));

        // 논리적 삭제
        member.updateDeleted(true);
        memberRepository.save(member);

        logout(request);
    }
}

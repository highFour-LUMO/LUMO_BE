package com.highFour.LUMO.member.service;



import com.highFour.LUMO.common.exceptionType.MemberExceptionType;
import com.highFour.LUMO.member.dto.MemberSignUpReq;
import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.jwt.service.JwtService;
import com.highFour.LUMO.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



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

    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String accessToken = jwtService.extractAccessToken(request).orElse(null);
        if (accessToken != null) {
            String email = jwtService.extractEmail(accessToken).orElse(null);
            if (email != null) {
                jwtService.deleteRefreshToken(email); // Redis에서 Refresh Token 삭제
                log.info("✅ 로그아웃 성공 - Redis Refresh Token 삭제 완료: {}", email);
            }
        }
    }
}

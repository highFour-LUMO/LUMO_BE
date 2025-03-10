package com.highFour.LUMO.member.service;



import com.highFour.LUMO.common.exceptionType.MemberExceptionType;
import com.highFour.LUMO.common.exceptionType.TokenExceptionType;
import com.highFour.LUMO.member.dto.MemberInfoRes;
import com.highFour.LUMO.member.dto.MemberPasswordUpdateReq;
import com.highFour.LUMO.member.dto.MemberSignUpReq;
import com.highFour.LUMO.member.dto.MemberUpdateInfoReq;
import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.jwt.service.JwtService;
import com.highFour.LUMO.member.repository.MemberRepository;
import com.highFour.LUMO.member.smtp.util.RedisUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final RedisUtil redisUtil;


    public void signUp(MemberSignUpReq memberSignUpReq) {
        String email = memberSignUpReq.email();

        // 이메일 인증 여부 확인 (Redis에서 조회)
        String isVerified = redisUtil.getData("email_verified:" + email);
        if (isVerified == null || !isVerified.equals("true")) {
            throw new ResponseStatusException(MemberExceptionType.NEED_TO_EMAIL_AUTH.httpStatus(),
                    MemberExceptionType.NEED_TO_EMAIL_AUTH.message());
        }

        // 이메일 중복 체크
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(MemberExceptionType.NOT_A_NEW_MEMBER.httpStatus(),
                    MemberExceptionType.NOT_A_NEW_MEMBER.message());
        }

        // 닉네임 중복 체크
        if (memberRepository.findByNickname(memberSignUpReq.nickname()).isPresent()) {
            throw new ResponseStatusException(MemberExceptionType.NOT_A_NEW_NICKNAME.httpStatus(),
                    MemberExceptionType.NOT_A_NEW_NICKNAME.message());
        }

        // 회원 정보 저장
        Member member = memberSignUpReq.toEntity(passwordEncoder);
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
            log.info(" Access Token 블랙리스트 추가 완료 (만료 시간: {}ms)", expiration);
        } else {
            log.warn(" Access Token이 이미 만료됨 - 블랙리스트에 저장하지 않음");
        }

        // Refresh Token 삭제
        if (jwtService.getRefreshTokenFromRedis(email) != null) {
            jwtService.deleteRefreshToken(email);
            log.info(" Refresh Token 삭제 완료: {}", email);
        } else {
            log.warn(" Refresh Token이 이미 삭제되었거나 없음: {}", email);
        }

        log.info(" 로그아웃 성공 - 사용자 이메일: {}", email);
    }


    public void deactivateMember(Long id, HttpServletRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(MemberExceptionType.MEMBER_NOT_FOUND.httpStatus(),
                        MemberExceptionType.MEMBER_NOT_FOUND.message()));

        member.updateDeleted(true);
        memberRepository.save(member);

        logout(request);
    }


    public MemberInfoRes memberInfo(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(MemberExceptionType.MEMBER_NOT_FOUND.httpStatus(),
                        MemberExceptionType.MEMBER_NOT_FOUND.message()));

        return MemberInfoRes.fromEntity(member);
    }

    public MemberUpdateInfoReq updateMemberInfo(MemberUpdateInfoReq updateInfo, Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(MemberExceptionType.MEMBER_NOT_FOUND.httpStatus(),
                        MemberExceptionType.MEMBER_NOT_FOUND.message()));

        member.updateNickname(updateInfo.nickname());
        member.updateProfileUrl(updateInfo.profileImageUrl());

        return MemberUpdateInfoReq.newInfo(member);
    }

    @Transactional
    public void changePassword(Long id, MemberPasswordUpdateReq req, PasswordEncoder passwordEncoder, HttpServletRequest request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(MemberExceptionType.MEMBER_NOT_FOUND.httpStatus(),
                        MemberExceptionType.MEMBER_NOT_FOUND.message()));

        // 현재 비밀번호 검증
        if (!passwordEncoder.matches(req.currentPassword(), member.getPassword())) {
            throw new ResponseStatusException(MemberExceptionType.PASSWORD_MISMATCH.httpStatus(),
                    MemberExceptionType.PASSWORD_MISMATCH.message());
        }

        // 새 비밀번호 & 확인 비밀번호 일치 검사
        if (!req.newPassword().equals(req.confirmNewPassword())) {
            throw new ResponseStatusException(MemberExceptionType.PASSWORD_CONFIRM_MISMATCH.httpStatus(),
                    MemberExceptionType.PASSWORD_CONFIRM_MISMATCH.message());
        }

        // 비밀번호 변경
        Member updatedMember = req.updatePassword(member, passwordEncoder);
        memberRepository.save(updatedMember);
        logout(request);
    }
}

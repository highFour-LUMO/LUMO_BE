package com.highFour.LUMO.member.login.handler;


import com.highFour.LUMO.common.exceptionType.MemberExceptionType;
import com.highFour.LUMO.member.entity.DelYn;
import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.jwt.service.JwtService;
import com.highFour.LUMO.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.server.ResponseStatusException;


@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    @Value("${jwt.access.expiration}")
    private String accessTokenExpiration;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        String email = extractUsername(authentication); // 인증 정보에서 Username(email) 추출

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(MemberExceptionType.MEMBER_NOT_FOUND.httpStatus(),
                        MemberExceptionType.MEMBER_NOT_FOUND.message()));

        // 삭제된 회원인지 확인
        if (member.getDelYn() == DelYn.Y) {
            log.warn("로그인 시도 실패: 탈퇴한 회원 (이메일: {})", email);
            throw new ResponseStatusException(MemberExceptionType.DELETED_MEMBER.httpStatus(),
                    MemberExceptionType.DELETED_MEMBER.message());
        }

        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.saveRefreshTokenToRedis(email, refreshToken);

        member.updateRefreshToken(refreshToken);
        memberRepository.saveAndFlush(member);

        log.info("로그인 성공 - 이메일: {}", email);
        log.info("발급된 AccessToken: {}", accessToken);
        log.info("발급된 AccessToken 만료 기간: {}", accessTokenExpiration);
    }



    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
package com.highFour.LUMO.member.service;

import com.highFour.LUMO.common.domain.JwtToken;
import com.highFour.LUMO.common.exception.BaseCustomException;
import com.highFour.LUMO.common.exceptionType.MemberExceptionType;
import com.highFour.LUMO.common.service.JwtService;
import com.highFour.LUMO.member.dto.*;
import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.entity.Role;
import com.highFour.LUMO.member.entity.SocialType;
import com.highFour.LUMO.member.oauth.google.service.GoogleService;
import com.highFour.LUMO.member.oauth.kakao.service.KakaoService;
import com.highFour.LUMO.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final KakaoService kakaoService;
    private final GoogleService googleService;
    private final JwtService jwtService;

    @Transactional
    public SignInRes signIn(final String socialAccessToken, final SocialType socialType) {
        MemberInfoServiceRes signedMember = getMemberInfo(socialType, socialAccessToken);
        // DB 에서 회원 찾기
        Optional<Member> optionalMember = memberRepository.findBySocialIdAndSocialType(signedMember.socialId(), socialType);
        Member member = optionalMember.orElseThrow(()->new BaseCustomException(MemberExceptionType.NEED_TO_REGISTER));
        JwtToken jwtToken = jwtService.issueToken(member.getId(), Role.USER.toString());
        return SignInRes.fromEntity(member, jwtToken);
    }

    @Transactional
    public SignUpRes signUp(String socialAccessToken, SignUpReq signUpReq) {
        MemberInfoServiceRes memberInfo = getMemberInfo(signUpReq.socialType(), socialAccessToken);
        Optional<Member> existingMember = memberRepository.findBySocialIdAndSocialType(memberInfo.socialId(), memberInfo.socialType());
        if (existingMember.isPresent()) {
            throw new BaseCustomException(MemberExceptionType.NOT_A_NEW_MEMBER);
        }
        Member member = signUpReq.toEntity(memberInfo.socialId(), memberInfo.email());
        memberRepository.save(member);
        JwtToken jwtToken = jwtService.issueToken(member.getId(), Role.USER.toString());

        return SignUpRes.fromEntity(member, jwtToken);
    }

    private MemberInfoServiceRes getMemberInfo(SocialType socialType, String socialAccessToken) {
        return switch (socialType) {
            case KAKAO -> kakaoService.getMemberInfo(socialAccessToken);
            case GOOGLE -> googleService.getMemberInfo(socialAccessToken);
            default -> throw new BaseCustomException(MemberExceptionType.INVALID_SOCIAL_TYPE);
        };
    }

    @Transactional
    public void signOut(final Long memberId) {
        jwtService.deleteRefreshToken(memberId);
    }

    @Transactional
    public void withdraw(Long memberId) {
        Member member = memberRepository.findByIdOrThrow(memberId);
        memberRepository.deleteById(member.getId());
        jwtService.deleteRefreshToken(member.getId());
    }

    @Transactional
    public MemberInfoRes getMemberInfo(Long memberId) {
        Member member = memberRepository.findByIdOrThrow(memberId);

        return MemberInfoRes.fromEntity(member);
    }



    @Transactional
    public MemberUpdateInfoReq updateMemberInfo(MemberUpdateInfoReq updateInfo, Long memberId) {
        Member member = memberRepository.findByIdOrThrow(memberId);
        member.updateprofileUrl(updateInfo.profileUrl());

        return MemberUpdateInfoReq.newInfo(member);
    }

}

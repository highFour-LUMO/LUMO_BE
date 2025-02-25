package com.highFour.LUMO.member.dto;

import com.highFour.LUMO.common.domain.JwtToken;
import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.entity.Role;
import com.highFour.LUMO.member.entity.SocialType;
import lombok.Builder;

@Builder
public record SignInRes(Long memberId,
                        String name,
                        String email,
                        String socialId,
                        SocialType socialType,
                        String role,
                        String accessToken,
                        String refreshToken){
    public static SignInRes fromEntity(Member member, JwtToken jwtToken){
        return SignInRes.builder()
                .memberId(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .socialType(member.getSocialType())
                .socialId(member.getSocialId())
                .role(Role.USER.toString())
                .accessToken(jwtToken.accessToken())
                .refreshToken(jwtToken.refreshToken())
                .build();
    }
}

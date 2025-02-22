package com.highFour.LUMO.member.dto;

import com.highFour.LUMO.common.domain.JwtToken;
import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.entity.MemberRole;
import com.highFour.LUMO.member.entity.SocialType;
import lombok.Builder;

@Builder
public record SignInResponse(Long memberId,
                             String name,
                             String email,
                             String socialId,
                             SocialType socialType,
                             String role,
                             String accessToken,
                             String refreshToken){
    public static SignInResponse fromEntity(Member member, JwtToken jwtToken){
        return SignInResponse.builder()
                .memberId(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .socialType(member.getSocialType())
                .socialId(member.getSocialId())
                .role(MemberRole.USER.toString())
                .accessToken(jwtToken.accessToken())
                .refreshToken(jwtToken.refreshToken())
                .build();
    }
}

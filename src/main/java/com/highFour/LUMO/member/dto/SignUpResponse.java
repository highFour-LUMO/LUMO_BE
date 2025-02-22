package com.highFour.LUMO.member.dto;

import com.highFour.LUMO.common.domain.JwtToken;
import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.entity.SocialType;
import lombok.Builder;

@Builder
public record SignUpResponse(Long memberId,
                             String name,
                             String email,
                             String socialId,
                             SocialType socialType,
                             String role,
                             String accessToken,
                             String refreshToken){
    public static SignUpResponse fromEntity(Member member, JwtToken jwtToken) {
        return SignUpResponse.builder()
                .memberId(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .socialId(member.getSocialId())
                .socialType(member.getSocialType())
                .accessToken(jwtToken.accessToken())
                .refreshToken(jwtToken.refreshToken())
                .build();
    }
}

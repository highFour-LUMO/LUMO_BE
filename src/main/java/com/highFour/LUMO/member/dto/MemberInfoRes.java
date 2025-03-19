package com.highFour.LUMO.member.dto;

import com.highFour.LUMO.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberInfoRes(
        String profileUrl,
        int point,
        String email,
        String nickname
) {
    public static MemberInfoRes fromEntity(Member member) {
        return MemberInfoRes.builder()
                .profileUrl(member.getProfileUrl())
                .point(member.getPoint())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .build();
    }
}

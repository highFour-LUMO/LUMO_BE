package com.highFour.LUMO.member.dto;

import com.highFour.LUMO.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberInfoRes(
        String profileUrl,
        String point,
        String email,
        String nickname,
        String name
) {
    public static MemberInfoRes fromEntity(Member member) {
        return MemberInfoRes.builder()
                .profileUrl(member.getProfileUrl())
                .point(String.valueOf(member.getPoint()))
                .email(member.getEmail())
                .nickname(member.getNickname())
                .name(member.getName())
                .build();
    }
}

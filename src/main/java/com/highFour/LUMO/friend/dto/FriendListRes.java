package com.highFour.LUMO.friend.dto;

import com.highFour.LUMO.member.entity.Member;
import lombok.Builder;

@Builder
public record FriendListRes(
        Long id,
        String name,
        String nickname,
        String email,
        String profileUrl
) {
    public static FriendListRes fromEntity(Member member) {
        return FriendListRes.builder()
                .id(member.getId())
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileUrl(member.getProfileUrl())
                .build();
    }
}

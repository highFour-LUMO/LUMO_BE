package com.highFour.LUMO.friend.dto;

import com.highFour.LUMO.friend.entity.Friend;
import com.highFour.LUMO.member.entity.Member;
import lombok.Builder;

@Builder
public record FriendListRes(
        Long id,
        String nickname,
        String email,
        String profileUrl
) {
    public static FriendListRes fromEntity(Member member) {
        return FriendListRes.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileUrl(member.getProfileUrl())
                .build();
    }

    public static FriendListRes toEntity(Friend friend, Member member) {
        Member friendMember = (friend.getMember1().equals(member)) ? friend.getMember2() : friend.getMember1();
        return fromEntity(friendMember);
    }
}

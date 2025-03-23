package com.highFour.LUMO.friend.dto;

import com.highFour.LUMO.friend.entity.Friend;
import com.highFour.LUMO.member.entity.Member;
import lombok.Builder;

@Builder
public record FriendReq(Long member1Id, Long member2Id) {

    public Friend toEntity(Member member1, Member member2) {
        return Friend.builder()
                .member1(member1)
                .member2(member2)
                .build();
    }
}

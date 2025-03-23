package com.highFour.LUMO.friend.dto;

import com.highFour.LUMO.friend.entity.FriendRequest;
import com.highFour.LUMO.friend.entity.FriendRequestStatus;
import com.highFour.LUMO.member.entity.Member;
import lombok.Builder;

@Builder
public record FriendRequestReq(Long receiverId) {

    public FriendRequest toEntity(Member sender, Member receiver) {
        return FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendRequestStatus.PENDING)
                .build();
    }
}

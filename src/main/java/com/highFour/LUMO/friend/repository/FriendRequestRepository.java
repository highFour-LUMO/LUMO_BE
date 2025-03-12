package com.highFour.LUMO.friend.repository;

import com.highFour.LUMO.friend.entity.Friend;
import com.highFour.LUMO.friend.entity.FriendRequest;
import com.highFour.LUMO.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    boolean existsBySenderAndReceiverOrReceiverAndSender(Member sender, Member receiver, Member receiver2, Member sender2);
}

package com.highFour.LUMO.friend.repository;

import com.highFour.LUMO.friend.entity.FriendRequest;
import com.highFour.LUMO.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    boolean existsBySenderAndReceiver(Member sender, Member receiver);
    Optional<FriendRequest> findBySenderAndReceiver(Member sender, Member receiver);
}

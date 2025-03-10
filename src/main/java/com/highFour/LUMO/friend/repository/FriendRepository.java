package com.highFour.LUMO.friend.repository;

import com.highFour.LUMO.friend.entity.Friend;
import com.highFour.LUMO.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByMember1OrMember2(Member member1, Member member2);
}

package com.highFour.LUMO.friend.service;

import com.highFour.LUMO.common.exceptionType.FriendExceptionType;
import com.highFour.LUMO.common.exceptionType.MemberExceptionType;
import com.highFour.LUMO.friend.dto.FriendListRes;
import com.highFour.LUMO.friend.entity.Friend;
import com.highFour.LUMO.friend.entity.FriendRequest;
import com.highFour.LUMO.friend.entity.FriendRequestStatus;
import com.highFour.LUMO.friend.repository.FriendRepository;
import com.highFour.LUMO.friend.repository.FriendRequestRepository;
import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendService {
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Transactional
    public List<FriendListRes> getFriendList(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(MemberExceptionType.MEMBER_NOT_FOUND.message()));

        List<Friend> friends = friendRepository.findByMember1OrMember2(member, member);

        if (friends == null || friends.isEmpty()) {
            throw new EntityNotFoundException(FriendExceptionType.FRIEND_NOT_FOUND.message());
        }

        return friends.stream()
                .map(friend -> {
                    Member friendMember = (friend.getMember1().equals(member)) ? friend.getMember2() : friend.getMember1();
                    return FriendListRes.fromEntity(friendMember);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void sendFriendRequest(Long senderId, Long receiverId) {
        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException(MemberExceptionType.MEMBER_NOT_FOUND.message()));

        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException(MemberExceptionType.MEMBER_NOT_FOUND.message()));

        boolean requestExists = friendRequestRepository.existsBySenderAndReceiver(sender, receiver) ||
                friendRequestRepository.existsBySenderAndReceiver(receiver, sender);
        if (requestExists) {
            throw new EntityNotFoundException(FriendExceptionType.ALREADY_REQUESTED.message());
        }

        boolean alreadyFriends = friendRepository.existsByMember1AndMember2(sender, receiver) ||
                friendRepository.existsByMember1AndMember2(receiver, sender);
        if (alreadyFriends) {
            throw new EntityNotFoundException(FriendExceptionType.ALREADY_FRIENDS.message());
        }

        FriendRequest friendRequest = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendRequestStatus.PENDING)
                .build();

        friendRequestRepository.save(friendRequest);
    }

}

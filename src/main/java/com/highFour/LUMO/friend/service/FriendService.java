package com.highFour.LUMO.friend.service;

import com.highFour.LUMO.common.exception.BaseCustomException;
import com.highFour.LUMO.common.exceptionType.FriendExceptionType;
import com.highFour.LUMO.common.exceptionType.MemberExceptionType;
import com.highFour.LUMO.friend.dto.FriendListRes;
import com.highFour.LUMO.friend.dto.FriendReq;
import com.highFour.LUMO.friend.dto.FriendRequestReq;
import com.highFour.LUMO.friend.entity.Friend;
import com.highFour.LUMO.friend.entity.FriendRequest;
import com.highFour.LUMO.friend.entity.FriendRequestStatus;
import com.highFour.LUMO.friend.repository.FriendRepository;
import com.highFour.LUMO.friend.repository.FriendRequestRepository;
import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendService {
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Transactional(readOnly = true)
    public List<FriendListRes> getFriendList() {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long memberId = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new BaseCustomException(MemberExceptionType.MEMBER_NOT_FOUND)).getId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(MemberExceptionType.MEMBER_NOT_FOUND.message()));

        List<Friend> friends = friendRepository.findByMember1OrMember2(member, member);

        if (friends == null || friends.isEmpty()) {
            throw new EntityNotFoundException(FriendExceptionType.FRIEND_NOT_FOUND.message());
        }

        return friends.stream()
                .map(friend -> FriendListRes.toEntity(friend, member))
                .collect(Collectors.toList());
    }

    @Transactional
    public void sendFriendRequest(FriendRequestReq requestDto) {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Member sender = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new BaseCustomException(MemberExceptionType.MEMBER_NOT_FOUND));

        Member receiver = memberRepository.findById(requestDto.receiverId())
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

        FriendRequest friendRequest = requestDto.toEntity(sender, receiver);
        friendRequestRepository.save(friendRequest);
    }

    @Transactional
    public void acceptFriendRequest(Long receiverId) {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long senderId = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new BaseCustomException(MemberExceptionType.MEMBER_NOT_FOUND)).getId();
        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException(MemberExceptionType.MEMBER_NOT_FOUND.message()));

        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException(MemberExceptionType.MEMBER_NOT_FOUND.message()));

        // 수락 || 거절은 요청을 받은 사람만 처리할 수 있으므로 sender, receiver 한 번만 조회
        FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiver(sender, receiver)
                .orElseThrow(() -> new EntityNotFoundException(FriendExceptionType.REQUEST_NOT_FOUND.message()));

        if (friendRequest.getStatus() != FriendRequestStatus.PENDING) {
            throw new IllegalStateException(FriendExceptionType.INVALID_REQUEST_STATUS.message());
        }

        // 요청 상태를 ACCEPTED로 변경
        friendRequest.acceptRequest();
        friendRequestRepository.save(friendRequest);

        FriendReq friendReq = new FriendReq(sender.getId(), receiver.getId());
        Friend friend = friendReq.toEntity(sender, receiver);
        friendRepository.save(friend);
    }

    @Transactional
    public void rejectFriendRequest(Long receiverId) {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long senderId = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new BaseCustomException(MemberExceptionType.MEMBER_NOT_FOUND)).getId();
        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException(MemberExceptionType.MEMBER_NOT_FOUND.message()));

        Member receiver = memberRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException(MemberExceptionType.MEMBER_NOT_FOUND.message()));

        FriendRequest friendRequest = friendRequestRepository.findBySenderAndReceiver(sender, receiver)
                .orElseThrow(() -> new EntityNotFoundException(FriendExceptionType.REQUEST_NOT_FOUND.message()));

        if (friendRequest.getStatus() != FriendRequestStatus.PENDING) {
            throw new IllegalStateException(FriendExceptionType.INVALID_REQUEST_STATUS.message());
        }

        // 요청 상태를 REJECTED로 변경
        friendRequest.rejectRequest();
        friendRequestRepository.save(friendRequest);
    }

    @Transactional
    public void unfriend(Long friendId) {
        String memberEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Long member1Id = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new BaseCustomException(MemberExceptionType.MEMBER_NOT_FOUND)).getId();
        Member member1 = memberRepository.findById(member1Id)
                .orElseThrow(() -> new EntityNotFoundException(MemberExceptionType.MEMBER_NOT_FOUND.message()));

        Member member2 = memberRepository.findById(friendId)
                .orElseThrow(() -> new EntityNotFoundException(MemberExceptionType.MEMBER_NOT_FOUND.message()));

        boolean alreadyFriends = friendRepository.existsByMember1AndMember2(member1, member2) ||
                friendRepository.existsByMember1AndMember2(member2, member1);
        if (!alreadyFriends) {
            throw new EntityNotFoundException(FriendExceptionType.FRIEND_NOT_FOUND.message());
        }

        boolean requestExists = friendRequestRepository.existsBySenderAndReceiver(member1, member2) ||
                friendRequestRepository.existsBySenderAndReceiver(member2, member1);

        friendRepository.deleteByMember1AndMember2(member1, member2);
        friendRepository.deleteByMember1AndMember2(member2, member1);

        if (requestExists) {
            friendRequestRepository.deleteBySenderAndReceiver(member1, member2);
            friendRequestRepository.deleteBySenderAndReceiver(member2, member1);
        }
    }




}

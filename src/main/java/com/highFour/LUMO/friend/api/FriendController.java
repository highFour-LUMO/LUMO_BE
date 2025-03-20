package com.highFour.LUMO.friend.api;

import com.highFour.LUMO.friend.service.FriendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/friends")
@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    // TODO: 추후 RestAPI 고려해 수정 고려 (전체 메서드 체크)

    // 로그인한 회원 자신의 친구 목록 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getFriends(@PathVariable Long memberId){
        return new ResponseEntity<>(friendService.getFriendList(memberId), HttpStatus.OK);
    }

    // 회원가입한 회원 간 친구 맺기 요청 보내기
    @PostMapping("/{senderId}/{receiverId}")
    public ResponseEntity<?> sendFriendRequest(@PathVariable Long senderId, @PathVariable Long receiverId) {
        friendService.sendFriendRequest(senderId, receiverId);
        return new ResponseEntity<>("친구 요청이 정상적으로 전송되었습니다", HttpStatus.OK);
    }

    // 친구 요청 수락
    @PostMapping("/accept/{senderId}/{receiverId}")
    public ResponseEntity<String> acceptFriendRequest(@PathVariable Long senderId, @PathVariable Long receiverId) {
        friendService.acceptFriendRequest(senderId, receiverId);
        return new ResponseEntity<>("친구 요청을 수락하였습니다.", HttpStatus.OK);
    }

    // 친구 요청 거절
    @PostMapping("/reject/{senderId}/{receiverId}")
    public ResponseEntity<String> rejectFriendRequest(@PathVariable Long senderId, @PathVariable Long receiverId) {
        friendService.rejectFriendRequest(senderId, receiverId);
        return new ResponseEntity<>("친구 요청을 거절하였습니다.", HttpStatus.OK);
    }

    @DeleteMapping("/{memberId}/unfriend/{friendId}")
    public ResponseEntity<String> unfriend(@PathVariable Long memberId, @PathVariable Long friendId) {
        friendService.unfriend(memberId, friendId);
        return new ResponseEntity<>("친구가 삭제 되었습니다.", HttpStatus.OK);
    }

}

package com.highFour.LUMO.friend.api;

import com.highFour.LUMO.friend.dto.FriendRequestReq;
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

    // 로그인한 회원 자신의 친구 목록 조회
    @GetMapping
    public ResponseEntity<?> getFriends(){
        return new ResponseEntity<>(friendService.getFriendList(), HttpStatus.OK);
    }

    // 회원가입한 회원 간 친구 맺기 요청 보내기
    @PostMapping("/request")
    public ResponseEntity<String> sendFriendRequest(@RequestBody FriendRequestReq requestDto) {
        friendService.sendFriendRequest(requestDto);
        return ResponseEntity.ok("친구 요청이 전송되었습니다.");
    }

    // 친구 요청 수락
    @PostMapping("/accept/{receiverId}")
    public ResponseEntity<String> acceptFriendRequest(@PathVariable Long receiverId) {
        friendService.acceptFriendRequest(receiverId);
        return new ResponseEntity<>("친구 요청을 수락하였습니다.", HttpStatus.OK);
    }

    // 친구 요청 거절
    @PostMapping("/reject/{receiverId}")
    public ResponseEntity<String> rejectFriendRequest(@PathVariable Long receiverId) {
        friendService.rejectFriendRequest(receiverId);
        return new ResponseEntity<>("친구 요청을 거절하였습니다.", HttpStatus.OK);
    }

    @DeleteMapping("/unfriend/{friendId}")
    public ResponseEntity<String> unfriend(@PathVariable Long friendId) {
        friendService.unfriend(friendId);
        return new ResponseEntity<>("친구가 삭제 되었습니다.", HttpStatus.OK);
    }

}

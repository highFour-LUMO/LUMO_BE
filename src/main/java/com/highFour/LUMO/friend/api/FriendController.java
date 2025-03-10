package com.highFour.LUMO.friend.api;

import com.highFour.LUMO.friend.dto.FriendListRes;
import com.highFour.LUMO.friend.service.FriendService;
import com.highFour.LUMO.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/friends")
@RestController
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    // 로그인한 회원 자신의 친구 목록 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getFriends(@PathVariable Long memberId){
        return new ResponseEntity<>(friendService.getFriendList(memberId), HttpStatus.OK);
    }

}

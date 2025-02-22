package com.highFour.LUMO.member.controller;

import com.highFour.LUMO.common.service.JwtService;
import com.highFour.LUMO.member.dto.*;
import com.highFour.LUMO.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RequestMapping("/member")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtService jwtService;

    // 소셜 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestHeader("X-AUTH-TOKEN") final String authorizationHeader, @RequestBody final SignInRequest signInRequest) {
        String bearerToken = authorizationHeader.replace("Bearer ", "");
        SignInResponse signInResponse = memberService.signIn(bearerToken, signInRequest.socialType());
        return new ResponseEntity<>(signInResponse, HttpStatus.OK);
    }

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(@RequestHeader("X-AUTH-TOKEN") final String authorizationHeader, @RequestBody final SignUpRequest signUpRequest) {
        String socialAccessToken = authorizationHeader.replace("Bearer ", "");
        SignUpResponse signUpResponse = memberService.signUp(socialAccessToken, signUpRequest);

        return new ResponseEntity<>(signUpResponse, HttpStatus.CREATED);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<SignOutRequest> signOut(@RequestHeader("myId") Long memberId){
        memberService.signOut(memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withDraw(@RequestHeader("myId") Long memberId){
        memberService.withdraw(memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/member-info")
    public ResponseEntity<MemberInfoResponse> getMemberInfo(@RequestHeader("myId") Long memberId){
        MemberInfoResponse memberInfo =  memberService.getMemberInfo(memberId);
        return new ResponseEntity<>(memberInfo, HttpStatus.OK);
    }



    @PatchMapping("/update-info")
    public ResponseEntity<MemberUpdateInfoRequest> updateMemberInfo(@RequestBody MemberUpdateInfoRequest updateInfo, @RequestHeader("myId") Long memberId){
        memberService.updateMemberInfo(updateInfo, memberId);
        return new ResponseEntity<>(updateInfo, HttpStatus.OK);
    }


}

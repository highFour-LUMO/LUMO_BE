package com.highFour.LUMO.member.controller;

import com.highFour.LUMO.common.service.JwtService;
import com.highFour.LUMO.member.dto.*;
import com.highFour.LUMO.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequestMapping("/member")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtService jwtService;

    // 소셜 로그인
    @PostMapping("/sign-in")
    public ResponseEntity<SignInRes> signIn(@RequestHeader("X-AUTH-TOKEN") final String authorizationHeader, @RequestBody final SignInReq signInReq) {
        String bearerToken = authorizationHeader.replace("Bearer ", "");
        SignInRes signInRes = memberService.signIn(bearerToken, signInReq.socialType());
        return new ResponseEntity<>(signInRes, HttpStatus.OK);
    }

    // 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpRes> signUp(@RequestHeader("X-AUTH-TOKEN") final String authorizationHeader, @RequestBody final SignUpReq signUpReq) {
        String socialAccessToken = authorizationHeader.replace("Bearer ", "");
        SignUpRes signUpRes = memberService.signUp(socialAccessToken, signUpReq);

        return new ResponseEntity<>(signUpRes, HttpStatus.CREATED);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<SignOutReq> signOut(@RequestHeader("myId") Long memberId){
        memberService.signOut(memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<?> withDraw(@RequestHeader("myId") Long memberId){
        memberService.withdraw(memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/member-info")
    public ResponseEntity<MemberInfoRes> getMemberInfo(@RequestHeader("myId") Long memberId){
        MemberInfoRes memberInfo =  memberService.getMemberInfo(memberId);
        return new ResponseEntity<>(memberInfo, HttpStatus.OK);
    }



    @PatchMapping("/update-info")
    public ResponseEntity<MemberUpdateInfoReq> updateMemberInfo(@RequestBody MemberUpdateInfoReq updateInfo, @RequestHeader("myId") Long memberId){
        memberService.updateMemberInfo(updateInfo, memberId);
        return new ResponseEntity<>(updateInfo, HttpStatus.OK);
    }


}

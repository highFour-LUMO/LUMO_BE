package com.highFour.LUMO.member.api;

import com.highFour.LUMO.member.dto.*;
import com.highFour.LUMO.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입 (POST /member/sign-up)
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody MemberSignUpReq memberSignUpReq) {
        memberService.signUp(memberSignUpReq);
        return new ResponseEntity<>(memberSignUpReq, HttpStatus.OK);
    }

    // 로그아웃 (POST /member/logout)
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        memberService.logout(request);
        return new ResponseEntity<>(request,HttpStatus.OK);
    }

    // 회원 비활성화 (PATCH /member/deactivate)
    @PatchMapping("/deactivate")
    public ResponseEntity<?> deactivateMember(HttpServletRequest request) {
        memberService.deactivateMember(request);
        return new ResponseEntity<>(request,HttpStatus.OK);
    }

    // 회원 정보 조회 (GET /member)
    @GetMapping("")
    public ResponseEntity<?> getMemberInfo() {
        return new ResponseEntity<>(memberService.memberInfo(),HttpStatus.OK);
    }

    // 회원 정보 수정 (PATCH /member)
    @PatchMapping("")
    public ResponseEntity<?> updateInfo(@RequestBody MemberUpdateInfoReq memberUpdateInfoReq) {
        return new ResponseEntity<>(memberService.updateMemberInfo(memberUpdateInfoReq),HttpStatus.OK);
    }

    // 비밀번호 변경 (PATCH /member/password )
    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody MemberPasswordUpdateReq req, HttpServletRequest request) {
        memberService.changePassword(req, passwordEncoder, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    // 비밀번호 찾기(리셋) (PATCH /member/reset-password )
    @PatchMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody MemberPasswordResetReq req,HttpServletRequest request) {
        memberService.resetPassword(req, passwordEncoder, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

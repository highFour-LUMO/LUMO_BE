package com.highFour.LUMO.member.api;

import com.highFour.LUMO.member.dto.*;
import com.highFour.LUMO.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<MemberSignUpReq> signUp(@RequestBody MemberSignUpReq memberSignUpReq) {
        memberService.signUp(memberSignUpReq);
        return ResponseEntity.status(201).body(memberSignUpReq);
    }

    // 로그아웃 (POST /member/logout)
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        memberService.logout(request);
        return ResponseEntity.ok("로그아웃 !");
    }

    // 회원 비활성화 (PATCH /member/{id}/deactivate)
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateMember(@PathVariable Long id, HttpServletRequest request) {
        memberService.deactivateMember(id, request);
        return ResponseEntity.ok("회원 탈퇴 완료");
    }

    // 회원 정보 조회 (GET /member/{id})
    @GetMapping("/{id}")
    public ResponseEntity<MemberInfoRes> getMemberInfo(@PathVariable Long id) {
        MemberInfoRes memberInfo = memberService.memberInfo(id);
        return ResponseEntity.ok(memberInfo);
    }

    // 회원 정보 수정 (PATCH /member/{id})
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateInfo(@RequestBody MemberUpdateInfoReq memberUpdateInfoReq, @PathVariable Long id) {
        memberService.updateMemberInfo(memberUpdateInfoReq, id);
        return ResponseEntity.ok("회원 정보 수정 완료");
    }

    // 비밀번호 변경 (PATCH /member/password )
    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody MemberPasswordUpdateReq req, @RequestHeader("myId") Long id, HttpServletRequest request) {
        memberService.changePassword(id, req, passwordEncoder, request);
        return ResponseEntity.ok("비밀번호 변경 완료");
    }
    // 비밀번호 찾기(리셋) (PATCH /member/reset-password )
    @PatchMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody MemberPasswordResetReq req, @RequestHeader("myId") Long id, HttpServletRequest request) {
        log.info("Received reset-password request for ID: {}", id);
        memberService.resetPassword(id, req, passwordEncoder, request);
        return ResponseEntity.ok("비밀번호 리셋 완료");
    }

}

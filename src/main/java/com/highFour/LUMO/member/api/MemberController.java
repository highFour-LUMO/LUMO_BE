package com.highFour.LUMO.member.api;

import com.highFour.LUMO.member.dto.*;
import com.highFour.LUMO.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/sign-up")
    public ResponseEntity<MemberSignUpReq> signUp(@RequestBody MemberSignUpReq memberSignUpReq) {
        memberService.signUp(memberSignUpReq);
        return new ResponseEntity<>(memberSignUpReq, HttpStatus.OK);
    }

    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        memberService.logout(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}/delete")
    public ResponseEntity<?> deleteMember(@PathVariable Long id, HttpServletRequest request) {
        memberService.deleteMember(id, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

package com.highFour.LUMO.member.smtp.api;

import com.highFour.LUMO.member.smtp.dto.EmailCheckReq;
import com.highFour.LUMO.member.smtp.dto.EmailSendReq;
import com.highFour.LUMO.member.smtp.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    // 인증 이메일 전송 (회원가입 / 비밀번호 재설정)
    @PostMapping("/sendAuthEmail")
    public ResponseEntity <?> sendAuthEmail(@RequestBody EmailSendReq emailDto, @RequestParam String type) {
        log.info("인증 이메일 요청 - Type: {}, Email: {}", type, emailDto.email());
        return new ResponseEntity<>(emailService.sendAuthEmail(emailDto.email(), type), HttpStatus.OK);
    }

    // 인증 번호 확인 (회원가입 / 비밀번호 재설정)
    @PostMapping("/checkAuthNum")
    public ResponseEntity<?> checkAuthNum(@RequestBody EmailCheckReq emailCheckReq, @RequestParam String type) {
        log.info("인증 번호 확인 - Type: {}, Email: {}", type, emailCheckReq.email());
        return new ResponseEntity<>(emailService.checkAuthNumber(emailCheckReq.email(), emailCheckReq.authNum(), type), HttpStatus.OK);
    }
}

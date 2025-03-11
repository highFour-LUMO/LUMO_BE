package com.highFour.LUMO.member.smtp.api;

import com.highFour.LUMO.member.smtp.dto.EmailCheckReq;
import com.highFour.LUMO.member.smtp.dto.EmailSendReq;
import com.highFour.LUMO.member.smtp.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/sendEmail")
    public String mailSend(@RequestBody @Valid EmailSendReq emailDto) {
        return emailService.joinEmail(emailDto.email());
    }

    @PostMapping("/mailauthCheck")
    public String AuthCheck(@RequestBody @Valid EmailCheckReq emailCheckReq) {
        emailService.CheckAuthNum(emailCheckReq.email(), emailCheckReq.authNum());
        return "ok";
    }

    @PostMapping("/sendResetEmail")
    public String sendResetEmail(@RequestBody @Valid EmailSendReq emailDto) {
        log.info("비밀번호 재설정 이메일 요청: " + emailDto.email());
        return emailService.sendPasswordResetEmail(emailDto.email());
    }

    @PostMapping("/resetAuthCheck")
    public String resetAuthCheck(@RequestBody @Valid EmailCheckReq emailCheckReq) {
        emailService.checkPasswordResetAuth(emailCheckReq.email(), emailCheckReq.authNum());
        return "ok";
    }

}

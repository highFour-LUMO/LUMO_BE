package com.highFour.LUMO.member.smtp.api;

import com.highFour.LUMO.common.exceptionType.MemberExceptionType;
import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.repository.MemberRepository;
import com.highFour.LUMO.member.smtp.dto.EmailCheckReq;
import com.highFour.LUMO.member.smtp.dto.EmailSendReq;
import com.highFour.LUMO.member.smtp.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        System.out.println("이메일 인증 이메일 :" + emailDto.email());
        return emailService.joinEmail(emailDto.email());
    }

    @PostMapping("/mailauthCheck")
    public String AuthCheck(@RequestBody @Valid EmailCheckReq emailCheckReq) {
        emailService.CheckAuthNum(emailCheckReq.email(), emailCheckReq.authNum());
        return "ok";
    }

}

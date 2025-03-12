package com.highFour.LUMO.member.smtp.service;

import com.highFour.LUMO.common.exceptionType.MemberExceptionType;
import com.highFour.LUMO.member.smtp.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Random;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class EmailService {

    private int authNumber;

    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;
    private final PasswordEncoder passwordEncoder;

    // 임의의 6자리 양수를 반환합니다.
    public void makeRandomNumber() {
        authNumber = new Random().nextInt(900000) + 100000; // 100000 ~ 999999 범위
    }

    // 이메일을 전송합니다.
    public void mailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage(); // JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8"); // 이메일 메시지와 관련된 설정을 수행합니다.
            helper.setFrom(setFrom); // 이메일의 발신자 주소 설정
            helper.setTo(toMail); // 이메일의 수신자 주소 설정
            helper.setSubject(title); // 이메일의 제목을 설정
            helper.setText(content, true); // 이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
            mailSender.send(message);
        } catch (MessagingException e) { // 이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
            // 이러한 경우 MessagingException이 발생
            e.printStackTrace(); // e.printStackTrace()는 예외를 기본 오류 스트림에 출력하는 메서드
        }
        redisUtil.setDataExpire(Integer.toString(authNumber), toMail, 60 * 5L);
    }


    public String sendAuthEmail(String email, String type) {
        String emailAuthKey = "email_auth:" + email;
        String passwordResetAuthKey = "password_reset_auth:" + email;

        // 기존 인증 기록 삭제
        if (Objects.equals(type, "signUp")) {
            redisUtil.deleteData("email_verified:" + email);
            redisUtil.deleteData(emailAuthKey);
        } else if (Objects.equals(type, "passwordReset")) {
            redisUtil.deleteData(passwordResetAuthKey);
        }

        // 새로운 인증번호 생성
        makeRandomNumber();

        // 이메일 전송 관련 정보 설정
        String senderEmail = "highfourhighfour@gmail.com";
        String subject, content;

        if (Objects.equals(type, "signUp")) {
            subject = "회원 가입 인증 이메일입니다.";
            content = "LUMO를 방문해주셔서 감사합니다.<br><br>"
                    + "인증 번호는 <b>" + authNumber + "</b> 입니다.<br>"
                    + "인증이 완료되면 회원가입을 진행할 수 있습니다.";
        } else if (Objects.equals(type, "passwordReset")) {
            subject = "비밀번호 재설정 인증 이메일입니다.";
            content = "비밀번호 재설정을 요청하셨습니다.<br><br>"
                    + "인증 번호는 <b>" + authNumber + "</b> 입니다.<br>"
                    + "인증 후 새로운 비밀번호를 설정해주세요.";
        } else {
            throw new IllegalArgumentException("잘못된 요청 타입입니다.");
        }

        // 이메일 발송
        mailSend(senderEmail, email, subject, content);

        // 인증번호 저장 (5분간 유효)
        long expirationTime = 60 * 5L;
        if (Objects.equals(type, "signUp")) {
            redisUtil.setDataExpire(emailAuthKey, Integer.toString(authNumber), expirationTime);
        } else if (Objects.equals(type, "passwordReset")) {
            redisUtil.setDataExpire(passwordResetAuthKey, Integer.toString(authNumber), expirationTime);
        }

        return Integer.toString(authNumber);
    }



    public void checkAuthNumber(String email, String authNum, String type) {
        String authKey;
        String verifiedKey;

        // 인증 유형에 따라 Redis 키 설정
        if (Objects.equals(type, "signUp")) {
            authKey = "email_auth:" + email;
            verifiedKey = "email_verified:" + email;
        } else if (Objects.equals(type, "passwordReset")) {
            authKey = "password_reset_auth:" + email;
            verifiedKey = "password_reset_verified:" + email;
        } else {
            throw new IllegalArgumentException("잘못된 인증 타입입니다.");
        }

        // Redis에서 저장된 인증 번호 조회
        String storedAuthNum = redisUtil.getData(authKey);

        if (storedAuthNum == null) { // 인증 번호가 만료된 경우
            throw new ResponseStatusException(MemberExceptionType.AUTH_NUMBER_EXPIRED.httpStatus(),
                    MemberExceptionType.AUTH_NUMBER_EXPIRED.message());
        }

        if (!storedAuthNum.equals(authNum)) { // 인증 번호가 일치하지 않는 경우
            throw new ResponseStatusException(MemberExceptionType.FAIL_TO_AUTH.httpStatus(),
                    MemberExceptionType.FAIL_TO_AUTH.message());
        }

        // 인증 성공 시 해당 이메일을 인증된 상태로 저장 (10분 유지)
        redisUtil.setData(verifiedKey, "true", 600);
    }


}
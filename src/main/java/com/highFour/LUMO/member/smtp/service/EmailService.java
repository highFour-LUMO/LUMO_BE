package com.highFour.LUMO.member.smtp.service;

import com.highFour.LUMO.common.exceptionType.MemberExceptionType;
import com.highFour.LUMO.member.smtp.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Random;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    private int authNumber;
    @Autowired
    private RedisUtil redisUtil;

    // 임의의 6자리 양수를 반환합니다.
    public void makeRandomNumber() {
        Random r = new Random();
        String randomNumber = "";
        for (int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }
        authNumber = Integer.parseInt(randomNumber);
    }

    public String joinEmail(String email) {
        // 기존 인증 기록 삭제 (이전 인증 무효화)
        redisUtil.deleteData("email_verified:" + email);
        redisUtil.deleteData("email_auth:" + email);

        // 새로운 인증번호 생성
        makeRandomNumber();

        String setFrom = "highfourhighfour@gmail.com"; // 발신자 이메일
        String toMail = email;
        String title = "회원 가입 인증 이메일입니다.";
        String content =
                "LUMO를 방문해주셔서 감사합니다." +
                        "<br><br>" +
                        "인증 번호는 <b>" + authNumber + "</b> 입니다." +
                        "<br>" +
                        "인증이 완료되면 회원가입을 진행할 수 있습니다.";

        // 이메일 전송
        mailSend(setFrom, toMail, title, content);

        // 새로운 인증번호를 "email_auth:<이메일>" 키로 저장하여 기존 코드 무효화 (5분간 유지)
        redisUtil.setDataExpire("email_auth:" + email, Integer.toString(authNumber), 60 * 5L);

        return Integer.toString(authNumber);
    }

    @Transactional
    public void CheckAuthNum(String email, String authNum) {
        System.out.println(email + " service " + authNum);

        // Redis에서 이메일에 해당하는 인증 코드 조회
        String storedAuthNum = redisUtil.getData("email_auth:" + email);

        if (storedAuthNum == null) {
            throw new ResponseStatusException(MemberExceptionType.AUTH_NUMBER_EXPIRED.httpStatus(),
                    MemberExceptionType.AUTH_NUMBER_EXPIRED.message());
        }

        if (!storedAuthNum.equals(authNum)) {
            throw new ResponseStatusException(MemberExceptionType.FAIL_TO_AUTH.httpStatus(),
                    MemberExceptionType.FAIL_TO_AUTH.message());
        }

        // 이메일 인증 완료 여부를 Redis에 저장 (10분 유지)
        redisUtil.setData("email_verified:" + email, "true", 600);
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

}

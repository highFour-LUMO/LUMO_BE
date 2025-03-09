package com.highFour.LUMO.member.smtp.service;

import com.highFour.LUMO.common.exceptionType.MemberExceptionType;
import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.repository.MemberRepository;
import com.highFour.LUMO.member.smtp.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private final MemberRepository memberRepository;

    //임의의 6자리 양수를 반환합니다.
    public void makeRandomNumber() {
        Random r = new Random();
        String randomNumber = "";
        for(int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }

        authNumber = Integer.parseInt(randomNumber);
    }


    //mail을 어디서 보내는지, 어디로 보내는지 , 인증 번호를 html 형식으로 어떻게 보내는지 작성합니다.
    public String joinEmail(String email) {
        makeRandomNumber();
        String setFrom = "yohanhwang10@gmail.com"; // email-config에 설정한 자신의 이메일 주소를 입력
        String toMail = email;
        String title = "회원 가입 인증 이메일 입니다."; // 이메일 제목
        String content =
                "LUMO를 방문해주셔서 감사합니다." + 	//html 형식으로 작성 !
                        "<br><br>" +
                        "인증 번호는 " + authNumber + "입니다." +
                        "<br>" +
                        "인증됐다 호호호"; //이메일 내용 삽입
        mailSend(setFrom, toMail, title, content);
        return Integer.toString(authNumber);
    }

    //이메일을 전송합니다.
    public void mailSend(String setFrom, String toMail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");//이메일 메시지와 관련된 설정을 수행합니다.
            // true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
            helper.setFrom(setFrom);//이메일의 발신자 주소 설정
            helper.setTo(toMail);//이메일의 수신자 주소 설정
            helper.setSubject(title);//이메일의 제목을 설정
            helper.setText(content,true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
            mailSender.send(message);
        } catch (MessagingException e) {//이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
            // 이러한 경우 MessagingException이 발생
            e.printStackTrace();//e.printStackTrace()는 예외를 기본 오류 스트림에 출력하는 메서드
        }
        redisUtil.setDataExpire(Integer.toString(authNumber),toMail,60*5L);
    }

    @Transactional
    public void CheckAuthNum(String email, String authNum) {
        System.out.println(email + " service " + authNum);
        System.out.println("redis " + redisUtil.getData(authNum));

        // Redis에서 인증번호 확인
        String storedEmail = redisUtil.getData(authNum);
        if (storedEmail == null || !storedEmail.equals(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다.");
        }

        // 인증이 완료되면 해당 이메일의 계정을 찾아 isVerified 값을 true로 변경
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(MemberExceptionType.MEMBER_NOT_FOUND.httpStatus(),
                        MemberExceptionType.MEMBER_NOT_FOUND.message()));

        member.updateIsVerified(true); // isVerified 값 변경
        // @Transactional 덕분에 변경 사항이 자동 저장됨 (save() 호출 필요 없음)
    }

}
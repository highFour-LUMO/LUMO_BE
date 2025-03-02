package com.highFour.LUMO.member.service;


import com.highFour.LUMO.member.dto.SignUpReq;
import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.entity.Role;
import com.highFour.LUMO.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpReq signUpReq) throws Exception {

        if (memberRepository.findByEmail(signUpReq.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        if (memberRepository.findByNickname(signUpReq.getNickname()).isPresent()) {
            throw new Exception("이미 존재하는 닉네임입니다.");
        }

        Member member = Member.builder()
                .email(signUpReq.getEmail())
                .password(signUpReq.getPassword())
                .nickname(signUpReq.getNickname())
                .role(Role.GUEST)
                .build();

        member.passwordEncode(passwordEncoder);
        memberRepository.save(member);
    }
}

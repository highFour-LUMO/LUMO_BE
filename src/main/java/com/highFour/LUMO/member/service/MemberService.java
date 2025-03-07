package com.highFour.LUMO.member.service;


import com.highFour.LUMO.common.exceptionType.MemberExceptionType;
import com.highFour.LUMO.member.dto.MemberSignUpReq;
import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(MemberSignUpReq memberSignUpReq) {
        if (memberRepository.findByEmail(memberSignUpReq.email()).isPresent()) {
            throw new ResponseStatusException(MemberExceptionType.NOT_A_NEW_MEMBER.httpStatus(), MemberExceptionType.NOT_A_NEW_MEMBER.message());
        }

        if (memberRepository.findByNickname(memberSignUpReq.nickname()).isPresent()) {
            throw new ResponseStatusException(MemberExceptionType.NOT_A_NEW_NICKNAME.httpStatus(), MemberExceptionType.NOT_A_NEW_NICKNAME.message());
        }

        Member member = memberSignUpReq.toEntity();
        member.passwordEncode(passwordEncoder);
        memberRepository.save(member);
    }
}

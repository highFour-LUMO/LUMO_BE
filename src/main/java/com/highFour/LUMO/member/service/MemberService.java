package com.highFour.LUMO.member.service;

import com.highFour.LUMO.member.dto.MemberCreateReqDto;
import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private void validateRegistration(MemberCreateReqDto createReqDto) {
        if (createReqDto.password().length() <= 7) {
            throw new RuntimeException("비밀번호는 8자 이상이어야 합니다.");
        }

        if (memberRepository.existsByEmail(createReqDto.email())  ) {
            throw new RuntimeException("이미 사용중인 이메일 입니다.");
        }

    }
    public void register(MemberCreateReqDto createReqDto) {
        validateRegistration(createReqDto);

        Member member = createReqDto.toEntity(passwordEncoder.encode(createReqDto.password()));
        memberRepository.save(member);
    }
}

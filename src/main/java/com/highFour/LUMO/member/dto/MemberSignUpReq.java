package com.highFour.LUMO.member.dto;


import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.entity.Role;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
public record MemberSignUpReq(String password, String nickname, String email, String name, String profileImageUrl) {
    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(this.email)
                .name(this.name)
                .password(passwordEncoder.encode(this.password)) // 비밀번호 암호화
                .nickname(this.nickname)
                .profileUrl(this.profileImageUrl)
                .role(Role.MEMBER)
                .build();
    }
}
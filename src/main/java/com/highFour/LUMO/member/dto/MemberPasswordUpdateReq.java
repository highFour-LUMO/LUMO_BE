package com.highFour.LUMO.member.dto;

import com.highFour.LUMO.member.entity.Member;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
public record MemberPasswordUpdateReq(
        String currentPassword,
        String newPassword,
        String confirmNewPassword
) {
    public Member updatePassword(Member member, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .password(passwordEncoder.encode(this.newPassword))
                .nickname(member.getNickname())
                .profileUrl(member.getProfileUrl())
                .role(member.getRole())
                .build();
    }
}

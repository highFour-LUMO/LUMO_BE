package com.highFour.LUMO.member.dto;


import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.entity.Role;
import lombok.Builder;

@Builder
public record MemberSignUpReq(String password, String nickname, String email) {
    public Member toEntity() {
        return Member.builder()
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .role(Role.GUEST)
                .build();
    }
}


package com.highFour.LUMO.member.dto;


import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.entity.SocialType;

public record SignUpRequest(SocialType socialType,
                            String name
) {
    public Member toEntity(String socialId, String email) {
        return Member.builder()
                .socialType(this.socialType)
                .name(this.name)
                .email(email)
                .socialId(socialId)
                .build();
    }
}

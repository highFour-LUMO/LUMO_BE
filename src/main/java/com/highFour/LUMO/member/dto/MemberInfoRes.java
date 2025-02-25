package com.highFour.LUMO.member.dto;

import com.highFour.LUMO.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberInfoRes(String name, String email, String phone, String address, String addressDetail, String zipcode) {
    public static MemberInfoRes fromEntity(Member member) {
        return MemberInfoRes.builder()
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }
}

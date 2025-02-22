package com.highFour.LUMO.member.dto;

import com.highFour.LUMO.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberInfoResponse(String name, String email, String phone, String address, String addressDetail, String zipcode) {
    public static MemberInfoResponse fromEntity(Member member) {
        return MemberInfoResponse.builder()
                .name(member.getName())
                .email(member.getEmail())
                .build();
    }
}

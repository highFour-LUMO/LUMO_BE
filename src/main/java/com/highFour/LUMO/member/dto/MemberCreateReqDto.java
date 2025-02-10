package com.highFour.LUMO.member.dto;

import com.highFour.Hand2Hand.domain.member.entity.Address;
import com.highFour.Hand2Hand.domain.member.entity.Member;

import lombok.Builder;

@Builder
public record MemberCreateReqDto(
        String name,
        String nickname,
        String password,
        String email,
        String phone,
        String profileImgUrl,
        Address address) {

    public static MemberCreateReqDto fromEntity(Member member){
        return MemberCreateReqDto.builder()
                .name(member.getName())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .email(member.getEmail())
                .phone(member.getPhone())
                .profileImgUrl(member.getProfileImgUrl())
                .address(member.getAddress())
                .build();
    }
}

package com.highFour.LUMO.member.dto;


import com.highFour.LUMO.member.entity.Member;
import lombok.Builder;


@Builder
public record MemberCreateReqDto(
        String name,
        String nickname,
        String password,
        String email,
        String phone,
        String profileImgUrl) {

    public static MemberCreateReqDto toEntity(Member member){
        return MemberCreateReqDto.builder()
                .name(member.getName())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .email(member.getEmail())
                .profileImgUrl(member.getProfileImgUrl())
                .build();
    }
}

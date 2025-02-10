package com.highFour.LUMO.member.dto;

import com.highFour.Hand2Hand.domain.member.entity.Address;
import com.highFour.Hand2Hand.domain.member.entity.Member;

import lombok.Builder;

@Builder
public record MemberUpdateReqDto(
        String nickname,
        String profileImgUrl,
        Address address,
        String phone) {
    public static MemberUpdateReqDto fromEntity(Member member){
        return MemberUpdateReqDto.builder()
                .nickname(member.getNickname())
                .profileImgUrl(member.getProfileImgUrl())
                .address(member.getAddress())
                .phone(member.getPhone())
                .build();
    }

}

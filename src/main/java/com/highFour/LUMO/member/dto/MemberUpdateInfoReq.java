package com.highFour.LUMO.member.dto;


import com.highFour.LUMO.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberUpdateInfoReq(String nickname, String profileImageUrl) {
    public static MemberUpdateInfoReq newInfo (Member member){
        return MemberUpdateInfoReq.builder()
                .nickname(member.getNickname())
                .profileImageUrl(member.getProfileUrl())
                .build();
    }
}

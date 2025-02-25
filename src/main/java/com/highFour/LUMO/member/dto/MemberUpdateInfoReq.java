package com.highFour.LUMO.member.dto;

import com.highFour.LUMO.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberUpdateInfoReq(String profileUrl) {

    public static MemberUpdateInfoReq newInfo(Member member){
        return MemberUpdateInfoReq.builder()
                .profileUrl(member.getProfileUrl())
                .build();
    }
}

package com.highFour.LUMO.member.dto;

import com.highFour.LUMO.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberUpdateInfoRequest(String profileUrl) {

    public static MemberUpdateInfoRequest newInfo(Member member){
        return MemberUpdateInfoRequest.builder()
                .profileUrl(member.getProfileUrl())
                .build();
    }
}

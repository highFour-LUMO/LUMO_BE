package com.highFour.LUMO.member.dto;



import com.highFour.LUMO.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberUpdateReqDto(
        String nickname,
        String profileImgUrl,
        String password) {
    public static MemberUpdateReqDto fromEntity(Member member){
        return MemberUpdateReqDto.builder()
                .nickname(member.getNickname())
                .profileImgUrl(member.getProfileImgUrl())
                .password(member.getPassword())
                .build();
    }

}

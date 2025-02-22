package com.highFour.LUMO.member.dto;


import com.highFour.LUMO.member.entity.SocialType;

public record MemberInfoServiceResponse(String socialId, SocialType socialType, String email) {
}

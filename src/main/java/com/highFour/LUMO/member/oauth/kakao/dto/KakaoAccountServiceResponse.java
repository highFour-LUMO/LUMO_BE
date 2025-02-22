package com.highFour.LUMO.member.oauth.kakao.dto;


import com.highFour.LUMO.member.entity.SocialType;

public record KakaoAccountServiceResponse(String socialId, SocialType socialType, String email) {
}

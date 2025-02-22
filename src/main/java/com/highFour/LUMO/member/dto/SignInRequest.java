package com.highFour.LUMO.member.dto;


import com.highFour.LUMO.member.entity.SocialType;

public record SignInRequest(SocialType socialType) {
}

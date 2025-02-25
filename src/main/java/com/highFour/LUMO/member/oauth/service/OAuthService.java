package com.highFour.LUMO.member.oauth.service;


import com.highFour.LUMO.member.dto.MemberInfoServiceRes;

public interface OAuthService {
    MemberInfoServiceRes getMemberInfo(final String accessToken);
}

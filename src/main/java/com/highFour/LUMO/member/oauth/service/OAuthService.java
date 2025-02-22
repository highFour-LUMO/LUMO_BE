package com.highFour.LUMO.member.oauth.service;


import com.highFour.LUMO.member.dto.MemberInfoServiceResponse;

public interface OAuthService {
    MemberInfoServiceResponse getMemberInfo(final String accessToken);
}

package com.highFour.LUMO.member.oauth.google.service;

import com.highFour.LUMO.common.exception.BaseCustomException;
import com.highFour.LUMO.member.dto.MemberInfoServiceResponse;
import com.highFour.LUMO.member.oauth.google.config.GoogleOAuthConfig;
import com.highFour.LUMO.member.oauth.google.dto.GoogleGetMemberInfoServiceResponse;
import com.highFour.LUMO.member.oauth.service.OAuthService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static com.highFour.LUMO.common.exceptionType.MemberExceptionType.FAIL_TO_AUTH;
import static com.highFour.LUMO.member.entity.SocialType.GOOGLE;
import static com.highFour.LUMO.member.oauth.google.config.GoogleOAuthConfig.AUTHORIZATION;
import static com.highFour.LUMO.member.oauth.google.config.GoogleOAuthConfig.GOOGLE_USER_INFO_URI;

@RequiredArgsConstructor
@Service
public class GoogleService implements OAuthService {

    private final GoogleOAuthConfig googleOAuthConfig;

    public MemberInfoServiceResponse getMemberInfo(final String accessToken) {
        GoogleGetMemberInfoServiceResponse response = null;
        try {
            RestClient restClient = RestClient.create();
            response = restClient.get()
                    .uri(GOOGLE_USER_INFO_URI)
                    .header(AUTHORIZATION, "Bearer " + accessToken)
                    .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError,
                            (googleRequest, googleResponse) -> {
                                throw new BaseCustomException(FAIL_TO_AUTH);
                            })
                    .body(GoogleGetMemberInfoServiceResponse.class);
        } catch(Exception e) {
            throw new BaseCustomException(FAIL_TO_AUTH);
        }

        assert response != null;
        return new MemberInfoServiceResponse(response.id(), GOOGLE, response.email());
    }

}

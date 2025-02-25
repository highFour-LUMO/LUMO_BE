package com.highFour.LUMO.member.oauth.kakao.service;

import com.highFour.LUMO.common.exception.BaseCustomException;
import com.highFour.LUMO.member.dto.MemberInfoServiceRes;
import com.highFour.LUMO.member.entity.SocialType;
import com.highFour.LUMO.member.oauth.kakao.config.KakaoOAuthConfig;
import com.highFour.LUMO.member.oauth.kakao.dto.KakaoGetMemberInfoServiceResponse;
import com.highFour.LUMO.member.oauth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static com.highFour.LUMO.common.exceptionType.MemberExceptionType.FAIL_TO_AUTH;


@RequiredArgsConstructor
@Service
public class KakaoService implements OAuthService {

    private final KakaoOAuthConfig kakaoOAuthConfig;

    // 카카오 API 에 로그인 요청을 보내고 회원 정보를 가져오기
    public MemberInfoServiceRes getMemberInfo(final String accessToken){
        KakaoGetMemberInfoServiceResponse response = null;
        RestClient restClient = RestClient.create();
        response = restClient.get()
                .uri(KakaoOAuthConfig.KAKAO_URI)
                .header(KakaoOAuthConfig.AUTHORIZATION, "Bearer " + accessToken)
                .header("Content-type","application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,(kakaoRequest, kakaoResponse) -> {
                    throw new BaseCustomException(FAIL_TO_AUTH);
                })
                .body(KakaoGetMemberInfoServiceResponse.class);
        if (response == null) {
            throw new BaseCustomException(FAIL_TO_AUTH);
        }
        return new MemberInfoServiceRes(response.id(), SocialType.KAKAO, response.kakao_account().email());
    }

}

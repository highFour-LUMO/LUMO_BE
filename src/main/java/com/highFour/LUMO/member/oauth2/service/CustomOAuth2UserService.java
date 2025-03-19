package com.highFour.LUMO.member.oauth2.service;

import com.highFour.LUMO.member.entity.Member;
import com.highFour.LUMO.member.entity.SocialType;
import com.highFour.LUMO.member.oauth2.CustomOAuth2User;
import com.highFour.LUMO.member.oauth2.OAuthAttributes;
import com.highFour.LUMO.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    private static final String KAKAO = "kakao";


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");


        /**
         * DefaultOAuth2UserService 객체를 생성하여, loadUser(userRequest)를 통해 DefaultOAuth2User 객체를 생성 후 반환
         * DefaultOAuth2UserService의 loadUser()는 소셜 로그인 API의 사용자 정보 제공 URI로 요청을 보내서
         * 사용자 정보를 얻은 후, 이를 통해 DefaultOAuth2User 객체를 생성 후 반환한다.
         * 결과적으로, OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저
         */
        // 1. OAuth2UserService를 사용하여 OAuth2User 객체 생성
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);


        /**
         * userRequest에서 registrationId 추출 후 registrationId으로 SocialType 저장
         * http://localhost:8080/oauth2/authorization/kakao에서 kakao가 registrationId
         * userNameAttributeName은 이후에 nameAttributeKey로 설정된다.
         */
        // 2. 로그인한 소셜 서비스 구분 (ex. kakao, google)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);

        // 3. OAuth 서비스에서 제공하는 유저 정보 가져오기
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();  // OAuth2 로그인 시 키(PK)가 되는 값
        Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 userInfo의 Json 값(유저 정보들)

        // 4. 소셜 타입에 따라 OAuthAttributes 객체 생성
        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

        // 5. 기존 회원인지 확인 (이메일 기반)
        // getUser() 메소드로 User 객체 생성 후 반환
        Member member = findOrCreateMember(extractAttributes, socialType);

        // 6. CustomOAuth2User 객체 반환
        // DefaultOAuth2User를 구현한 CustomOAuth2User 객체를 생성해서 반환
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().getKey())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                member.getEmail(),
                member.getRole()
        );
    }


    private SocialType getSocialType(String registrationId) {
        if(KAKAO.equals(registrationId)) {
            return SocialType.KAKAO;
        }
        return SocialType.GOOGLE;
    }

    private Member findOrCreateMember(OAuthAttributes attributes, SocialType socialType) {
        String email = attributes.getOauth2UserInfo().getEmail();
        String socialId = attributes.getOauth2UserInfo().getId();

        Member member = memberRepository.findByEmail(email).orElse(null);

        if (member != null) {
            // 기존 회원이 소셜 로그인 정보가 없을 경우 연동
            if (member.getSocialId() == null) {
                member.linkSocialAccount(socialId, socialType);
                memberRepository.save(member);
            }
            return member;
        }

        // 새로운 회원 생성
        Member newMember = attributes.toEntity(socialType, attributes.getOauth2UserInfo());
        return memberRepository.save(newMember);
    }

}

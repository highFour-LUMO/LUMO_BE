package com.highFour.LUMO.member.service;


import com.highFour.Hand2Hand.domain.member.entity.Member;
import com.highFour.Hand2Hand.domain.member.entity.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    // 초기화 이전 객체들 값이 ?
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String pictureURL;

    // 소셜에서 갖고온 값들 초기화진행
    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey,
                           String name, String email, String pictureURL) {

        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.pictureURL = pictureURL;
    }

    // registrationId 는 여러개의 소셜을 구분하는 용도
    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        if ("kakao".equals(registrationId)) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }


    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes) {

        System.out.println(attributes);
        System.out.println(attributes.toString());
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .pictureURL((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName,
                                           Map<String, Object> attributes) {
        System.out.println(attributes);
        System.out.println(attributes.toString()); // 찍어보면서 들어오는 값을 확인

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
//        System.out.println("여기서 부터닦ㅆ%!!!!!!!!!!!!!!!!!!!");
        kakaoAccount.get(profile.get("nickname"));

        return OAuthAttributes.builder()
                .name((String) profile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .pictureURL((String) profile.get("profile_image_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }



    public Member toEntity() {
        return Member.builder()
                .name(name)
                .email(email)
                .role(Role.USER)
                .build();
    }
}
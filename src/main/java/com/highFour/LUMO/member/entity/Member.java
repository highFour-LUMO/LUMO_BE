package com.highFour.LUMO.member.entity;

import com.highFour.LUMO.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Getter
@Table(name = "member")
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;


    private String password;


    private String nickname;

    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    private String socialId;

    private String profileUrl;

    @Builder.Default
    private int point = 0;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    private String fcmToken;

    @Enumerated(EnumType.STRING)
    private DelYn delYn = DelYn.N;

    public void updateFcmToken(String fcmToken){
        this.fcmToken = fcmToken;
    }

    public void linkSocialAccount(String socialId, SocialType socialType) {
        this.socialId = socialId;
        this.socialType = socialType;
    }
    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }
    public void updateDeleted() {
        this.delYn = DelYn.Y;
    }
    public void updateNickname(String updateNickname) {
        this.nickname = updateNickname;
    }
    public void updateProfileUrl(String updateProfileUrl) {
        this.profileUrl = updateProfileUrl;
    }
}
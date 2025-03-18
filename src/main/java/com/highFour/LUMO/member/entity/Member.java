package com.highFour.LUMO.member.entity;

import com.highFour.LUMO.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    private String name;

    private String password;


    private String nickname;

    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    private String socialId;

    private String profileUrl;

    private String point;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String refreshToken;

    private String fcmToken;

    @Builder.Default
    private boolean deleted = false;

    public void authorizeUser() {
        this.role = Role.MEMBER;
    }

    public void updateFcmToken(String fcmToken){
        this.fcmToken = fcmToken;
    }

    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    public void updateDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void updateNickname(String updateNickname) {
        this.nickname = updateNickname;
    }
    public void updateProfileUrl(String updateProfileUrl) {
        this.profileUrl = updateProfileUrl;
    }
    public void updatePassword(String newPassword, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(newPassword);
    }
}
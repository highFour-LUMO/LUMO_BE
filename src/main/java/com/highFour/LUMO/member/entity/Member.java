package com.highFour.LUMO.member.entity;

import com.highFour.LUMO.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String nickname;

    @NotNull
    private String password;

    @NotNull
    private String email;

    @NotNull

    private String profileImgUrl;

    @Builder.Default
    private boolean isVerified = false;


    public Member updateName(String temp) {
        this.name = temp;
        return this;
    }

    public Member updatePass(String uuidPass) {
        this.password = uuidPass;
        return this;
    }
}

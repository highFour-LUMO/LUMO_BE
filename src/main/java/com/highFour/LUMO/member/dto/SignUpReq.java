package com.highFour.LUMO.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SignUpReq {
    private String password;
    private String nickname;
    private String email;
}

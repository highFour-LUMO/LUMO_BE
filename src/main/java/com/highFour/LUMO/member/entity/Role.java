package com.highFour.LUMO.member.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    ADMIN("ROLE_ADMIN"), MEMBER("ROLE_MEMBER");

    private final String key;
}
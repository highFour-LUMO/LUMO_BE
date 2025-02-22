package com.highFour.LUMO.common.domain;

import lombok.Builder;


@Builder
public record JwtToken(String accessToken, String refreshToken) {

}
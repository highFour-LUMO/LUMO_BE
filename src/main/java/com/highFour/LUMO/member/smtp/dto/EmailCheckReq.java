package com.highFour.LUMO.member.smtp.dto;

import jakarta.validation.constraints.Email;

public record EmailCheckReq(@Email String email, String authNum) {
}

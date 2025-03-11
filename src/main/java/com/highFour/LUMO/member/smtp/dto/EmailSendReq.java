package com.highFour.LUMO.member.smtp.dto;


import jakarta.validation.constraints.Email;

public record EmailSendReq(
        @Email
        String email
) {}

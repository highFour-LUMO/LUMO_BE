package com.highFour.LUMO.member.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highFour.LUMO.common.response.CustomErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .name(HttpStatus.UNAUTHORIZED.name())
                .httpStatusCode(HttpStatus.UNAUTHORIZED.value())
                .message("유효하지 않은 토큰입니다.")
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}

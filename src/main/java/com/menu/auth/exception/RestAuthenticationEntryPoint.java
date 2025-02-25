package com.menu.auth.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * Spring Security 애플리케이션에서 발생하는 인증 실패에 대한 처리.
 * 401 HTTP 에러 응답
 */
@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(AuthErrorCode.AUTH_INVALID_TOKEN.getStatus().value());
        response.getWriter().write(authException.getMessage());
//        response.getWriter().write(Response.error(ErrorCode.INVALID_TOKEN.name()).toStream());
    }
}

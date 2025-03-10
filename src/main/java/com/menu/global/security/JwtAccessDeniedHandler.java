package com.menu.global.security;

import com.menu.global.exception.BaseException;
import com.menu.global.exception.GlobalErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        log.info("{ JwtAccessDeniedHandler } : 토큰 없음. 토큰 필요");
        throw BaseException.type(GlobalErrorCode.INVALID_USER); // 필요한 권한 x -> 403
    }
}

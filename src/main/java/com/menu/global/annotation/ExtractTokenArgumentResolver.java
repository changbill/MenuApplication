package com.menu.global.annotation;

import com.menu.auth.exception.AuthErrorCode;
import com.menu.auth.security.jwt.JwtProvider;
import com.menu.auth.utils.AuthorizationExtractor;
import com.menu.global.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class ExtractTokenArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtProvider jwtProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ExtractToken.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = AuthorizationExtractor.extractToken(request)
                .orElseThrow(() -> BaseException.type(AuthErrorCode.INVALID_PERMISSION));
        jwtProvider.validateToken(token);
        return token;
    }
}

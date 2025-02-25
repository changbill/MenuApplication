package com.menu.auth.repository;

import com.menu.auth.domain.CookieName;
import com.menu.auth.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

public class OAuth2AuthorizationRequestCookieRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    private static final int COOKIE_EXPIRE_SECONDS = 180;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request, CookieName.OAUTH2_AUTHORIZATION_REQUEST)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if(authorizationRequest == null) {
            deleteCookie(request, response);
            return;
        }

        CookieUtils.addCookie(
                response,
                CookieName.OAUTH2_AUTHORIZATION_REQUEST,
                CookieUtils.serialize(authorizationRequest),
                COOKIE_EXPIRE_SECONDS
        );

        String redirectUri = request.getParameter(CookieName.REDIRECT_URI.getValue());
        if(StringUtils.hasText(redirectUri))
            CookieUtils.addCookie(
                    response,
                    CookieName.REDIRECT_URI,
                    redirectUri,
                    COOKIE_EXPIRE_SECONDS
            );

        String mode = request.getParameter(CookieName.MODE.getValue());
        if(StringUtils.hasText(mode))
            CookieUtils.addCookie(
                    response,
                    CookieName.MODE,
                    mode,
                    COOKIE_EXPIRE_SECONDS
            );
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        deleteCookie(request, response);
        return this.loadAuthorizationRequest(request);
    }

    private static void deleteCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, CookieName.OAUTH2_AUTHORIZATION_REQUEST);
        CookieUtils.deleteCookie(request, response, CookieName.REDIRECT_URI);
        CookieUtils.deleteCookie(request, response, CookieName.MODE);
    }
}

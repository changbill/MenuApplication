package com.menu.auth.handler;

import com.menu.auth.domain.CookieName;
import com.menu.auth.repository.OAuth2AuthorizationRequestCookieRepository;
import com.menu.auth.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final OAuth2AuthorizationRequestCookieRepository authorizationRequestRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, IOException {
        String targetUrl = CookieUtils.getCookie(request, CookieName.REDIRECT_URI)
                .map(Cookie::getValue)
                .orElse("/");

        log.error(exception.getMessage(), exception);

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", exception.getLocalizedMessage())
                .build().toUriString();

        authorizationRequestRepository.removeAuthorizationRequest(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}

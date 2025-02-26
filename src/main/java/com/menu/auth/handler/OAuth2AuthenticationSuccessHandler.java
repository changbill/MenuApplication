package com.menu.auth.handler;

import com.menu.auth.domain.CookieName;
import com.menu.auth.domain.OAuth2UserInfo;
import com.menu.auth.domain.OAuth2UserInfoFactory;
import com.menu.auth.dto.UserPrincipal;
import com.menu.auth.exception.AuthErrorCode;
import com.menu.auth.repository.OAuth2AuthorizationRequestCookieRepository;
import com.menu.auth.security.jwt.AuthToken;
import com.menu.auth.security.jwt.JwtProvider;
import com.menu.auth.utils.CookieUtils;
import com.menu.global.config.properties.AppProperties;
import com.menu.global.exception.BaseException;
import com.menu.member.domain.MemberRefreshToken;
import com.menu.member.domain.Role;
import com.menu.member.domain.SocialType;
import com.menu.member.repository.MemberRefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;

/**
 * OAuth2 인증 성공 시 호출되는 핸들러.
 * 처음 프론트에서 백으로 로그인 요청 시 mode 쿼리 파라미터에 담긴 값에 따라 분기하여 처리.
 * mode 값이 login이면 사용자 정보를 DB에 저장하고, 서비스 자체 액세스 토큰, 리프레시 토큰을 생성하고 리프레시 토큰을 DB에 저장한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;
    private final AppProperties appProperties;
    private final MemberRefreshTokenRepository memberRefreshTokenRepository;
    private final OAuth2AuthorizationRequestCookieRepository authorizationRequestCookieRepository;

    private static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 7;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        if(response.isCommitted()) {
            log.debug("Response has already been committed. Unable to redirect to target url {}", targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, CookieName.REDIRECT_URI).map(Cookie::getValue);
        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw BaseException.type(AuthErrorCode.AUTH_INVALID_REDIRECT_URI);
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        SocialType socialType = SocialType.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());

        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(socialType, user.attributes());
        Collection<? extends GrantedAuthority> authorities = ((OidcUser) authentication.getPrincipal()).getAuthorities();
        Role role = isOwner(authorities, Role.OWNER.getAuthority()) ? Role.OWNER : Role.USER;

        AuthToken accessToken = jwtProvider.createAccessToken(userInfo.getEmail(), role.getAuthority());
        AuthToken refreshToken = jwtProvider.createRefreshToken(userInfo.getEmail());

        MemberRefreshToken memberRefreshToken = memberRefreshTokenRepository.findByEmail(userInfo.getEmail());
        if(memberRefreshToken == null) {
            memberRefreshToken = new MemberRefreshToken(userInfo.getEmail(), refreshToken.getToken());
            memberRefreshTokenRepository.save(memberRefreshToken);
        } else {
            memberRefreshToken.setRefreshToken(refreshToken.getToken());
        }
        log.debug("리프레시 토큰 {} : {}", userInfo, memberRefreshToken);

        CookieUtils.deleteCookie(request, response, CookieName.REFRESH_TOKEN);
        CookieUtils.addCookie(response, CookieName.REFRESH_TOKEN, refreshToken.getToken(), COOKIE_MAX_AGE);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", accessToken.getToken())
                .build()
                .toUriString();
    }

    private boolean isOwner(Collection<? extends GrantedAuthority> authorities, String authority) {
        if(authorities == null || authorities.isEmpty()) {
            return false;
        }

        for(GrantedAuthority grantedAuthority : authorities) {
            if(grantedAuthority.getAuthority().equals(authority)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        return appProperties.getOAuth2().getAuthorizedRedirectUris().stream()
                .anyMatch(authorizedRedirectUri -> {
                    URI authorizeURI = URI.create(authorizedRedirectUri);
                    return authorizeURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost()) && authorizeURI.getPort() == clientRedirectUri.getPort();
                });
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestCookieRepository.removeAuthorizationRequest(request, response);
    }
}

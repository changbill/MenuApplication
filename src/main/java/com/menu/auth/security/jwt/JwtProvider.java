package com.menu.auth.security.jwt;

import com.menu.auth.domain.ClaimsKey;
import com.menu.auth.dto.UserPrincipal;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtProvider {
    private final Environment environment;

    public String getSalt() {
        return environment.getProperty("jwt.secret.key");
    }

    public long getAccessExp() {
        return environment.getProperty("app.auth.accessExp", long.class);
    }

    public long getRefreshExp() {
        return environment.getProperty("app.auth.refreshExp", long.class);
    }

    // accessToken: 권한 부여
    public AuthToken createAccessToken(String email, String role) {
        return new AuthToken(email, role, getSalt(), getAccessExp());
    }

    // refreshToken: 인증으로 accessToken을 재발급 받는것이 주 관심사기 때문에 role을 필요로 하지 않는다.
    public AuthToken createRefreshToken(String email) {
        return new AuthToken(email, getSalt(), getRefreshExp());
    }

    public AuthToken convertAuthToken(String tokenStr) {
        return new AuthToken(tokenStr, getSalt());
    }

    public Authentication getAuthentication(AuthToken authToken) {
        authToken.validateToken();

        Claims claims = authToken.extractClaims();
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{claims.get(ClaimsKey.ROLE.getValue()).toString()})
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        UserPrincipal principal = UserPrincipal.of(getUserEmail(authToken), authorities, null);
        return new UsernamePasswordAuthenticationToken(principal, authorities);
    }

    public String getUserEmail(AuthToken authToken) {
        return Objects.requireNonNull(authToken.extractClaims()).get(ClaimsKey.EMAIL.getValue(), String.class);
    }

    public String getUserEmail(String token) {
        return getUserEmail(convertAuthToken(token));
    }

    public void validateToken(String token) {
        convertAuthToken(token).validateToken();
    }
}

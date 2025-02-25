package com.menu.auth.security.jwt;

import com.menu.auth.domain.ClaimsKey;
import com.menu.auth.dto.UserPrincipal;
import com.menu.auth.exception.AuthErrorCode;
import com.menu.global.exception.BaseException;
import com.menu.member.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("${jwt.secret.key}")
    private String salt;

    @Value("app.auth.accessExp")
    private long accessExp;

    @Value("app.auth.refreshExp")
    private long refreshExp;

    // accessToken: 권한 부여
    public AuthToken createAccessToken(String email, String role) {
        return new AuthToken(email, role, salt, accessExp);
    }

    // refreshToken: 인증으로 accessToken을 재발급 받는것이 주 관심사기 때문에 role을 필요로 하지 않는다.
    public AuthToken createRefreshToken(String email) {
        return new AuthToken(email, salt, refreshExp);
    }

    public AuthToken convertAuthToken(String tokenStr) {
        return new AuthToken(tokenStr, salt);
    }

    public Authentication getAuthentication(AuthToken authToken) {
        authToken.validateToken();

        Claims claims = authToken.extractClaims();
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(new String[]{claims.get(ClaimsKey.ROLE.getValue()).toString()})
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        UserPrincipal principal = UserPrincipal.of(getUserEmail(authToken), null, authorities);
        return new UsernamePasswordAuthenticationToken(principal, authorities);
    }

    public String getUserEmail(AuthToken authToken) {
        return Objects.requireNonNull(authToken.extractClaims()).get(ClaimsKey.EMAIL.getValue(), String.class);
    }

}

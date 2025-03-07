package com.menu.auth.utils;

import com.menu.auth.exception.AuthErrorCode;
import com.menu.global.exception.BaseException;
import com.menu.user.domain.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtProvider {
    @Value("${jwt.secret.key}")
    private String salt;
    private Key secretKey;

    // 만료시간 - 7일
    private final long accessExp = 1000L * 60 * 60 * 24 * 7;

    // 만료시간 - 14일
    private final long refreshExp = 1000L * 60 * 60 * 24 * 14;

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(Long id, Role role) {
        return createToken(id, role, accessExp);
    }

    public String createRefreshToken(Long id, Role role) {
        return createToken(id, role, refreshExp);
    }

    private String createToken(Long id, Role role, long exp) {
        Claims claims = Jwts.claims();
        claims.put("id", id);
        claims.put("role", role);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + exp))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Long getId(String token) {
        return getClaims(token)
                .getBody()
                .get("id", Long.class);
    }

    // Authorization Header를 통해 인증
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = getClaims(token);
            Date expiredDate = claims.getBody().getExpiration();
            Date now = new Date();
            return expiredDate.after(now);
        } catch (ExpiredJwtException e) {
            throw BaseException.type(AuthErrorCode.AUTH_EXPIRED_TOKEN);
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw BaseException.type(AuthErrorCode.AUTH_INVALID_TOKEN);
        }
    }

    private Jws<Claims> getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }
}

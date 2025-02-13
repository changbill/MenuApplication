package com.menu.auth.security;

import com.menu.auth.exception.AuthErrorCode;
import com.menu.global.exception.BaseException;
import com.menu.member.domain.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.security.Keys;
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

    @Value("app.auth.accessExp")
    private long accessExp;

    @Value("app.auth.refreshExp")
    private long refreshExp;

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(Long memberId, Role role) {
        return createToken(memberId, role, accessExp);
    }

    public String createRefreshToken(Long memberId, Role role) {
        return createToken(memberId, role, refreshExp);
    }

    private String createToken(Long memberId, Role role, long exp) {
        Claims claims = Jwts.claims();
        claims.put("id", memberId);
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

    public void validToken(String token) {
        try {
            getClaims(token);
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

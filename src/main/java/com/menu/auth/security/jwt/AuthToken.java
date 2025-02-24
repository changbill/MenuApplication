package com.menu.auth.security.jwt;

import com.menu.auth.exception.AuthErrorCode;
import com.menu.global.exception.BaseException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class AuthToken {

    @Getter
    private final String token;
    private final String salt;

    AuthToken(String email, String role, String salt, Long exp) {
        this.token = generateToken(email, role, salt, exp);
        this.salt = salt;
    }

    AuthToken(String email, String salt, Long exp) {
        this.token = generateToken(email, salt, exp);
        this.salt = salt;
    }

    public void validateToken() {
        extractClaims();
    }

    public boolean isExpired() {
        Date expiredDate = Objects.requireNonNull(extractClaims()).getExpiration();
        return expiredDate.before(new Date());
    }

    public Claims extractClaims() {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey(salt))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            throw BaseException.type(AuthErrorCode.AUTH_EXPIRED_TOKEN);
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            log.info("Invalid JWT token.");
            throw BaseException.type(AuthErrorCode.AUTH_INVALID_TOKEN);
        }
    }

    public Claims getExpiredClaims() {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey(salt))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token");
            return e.getClaims();
        }
        return null;
    }

    private String generateToken(String email, String salt, Long exp) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + exp))
                .signWith(getKey(salt), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateToken(String email, String role, String salt, Long exp) {
        Claims claims = Jwts.claims();
        claims.put("email", email);
        claims.put("role", role);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + exp))
                .signWith(getKey(salt), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

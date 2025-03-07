package com.menu.auth.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash("Token")
public class RefreshToken {
    public static final Long DEFAULT_TTL = 1209600L; // 14일

    @Id
    private Long id;

    private String refreshToken;

    @TimeToLive
    private Long expiration;

    private RefreshToken(Long id, String refreshToken) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.expiration = DEFAULT_TTL;
    }

    public static RefreshToken createRefreshToken(Long id, String refreshToken) {
        return new RefreshToken(id, refreshToken);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

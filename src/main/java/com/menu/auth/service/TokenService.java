package com.menu.auth.service;

import com.menu.auth.domain.RefreshToken;
import com.menu.auth.repository.RefreshTokenRedisRepository;
import com.menu.global.exception.BaseException;
import com.menu.auth.exception.AuthErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TokenService {
    private final RefreshTokenRedisRepository refreshTokenRedisRepository;

    @Transactional
    public void synchronizeRefreshToken(Long id, String refreshToken) {
        log.info("{ TokenService } : 토큰 발급 혹은 재발급 진입");
        refreshTokenRedisRepository.findById(id)
                .ifPresentOrElse(
                        token -> {
                            token.updateRefreshToken(refreshToken);
                            refreshTokenRedisRepository.save(token);
                        },
                        () -> refreshTokenRedisRepository.save(RefreshToken.createRefreshToken(id, refreshToken))
                );
        log.info("{ TokenService } : 토큰 발급 성공");
    }

    public RefreshToken findRefreshTokenById(Long id) {
        return refreshTokenRedisRepository.findById(id)
                .orElseThrow(() -> BaseException.type(AuthErrorCode.REFRESH_TOKEN_NOT_FOUND));
    }

    @Transactional
    public void reissueRefreshTokenByRtrPolicy(Long id, String newRefreshToken) {
        RefreshToken refreshToken = findRefreshTokenById(id);
        refreshToken.updateRefreshToken(newRefreshToken);
        refreshTokenRedisRepository.save(refreshToken);
    }

    @Transactional
    public void deleteRefreshTokenByUserId(Long id) {
        refreshTokenRedisRepository.deleteById(id);
    }

    public boolean isRefreshTokenExists(Long id, String refreshToken) {
        return refreshTokenRedisRepository.findById(id)
                .map(token -> token.getRefreshToken().equals(refreshToken))
                .orElse(false);
    }
}

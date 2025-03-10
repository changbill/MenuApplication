package com.menu.auth.service.owner;

import com.menu.auth.dto.TokenResponseDto;
import com.menu.auth.exception.AuthErrorCode;
import com.menu.auth.service.TokenService;
import com.menu.auth.utils.JwtProvider;
import com.menu.global.exception.BaseException;
import com.menu.user.domain.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OwnerTokenReissueService {
    private final TokenService tokenService;
    private final JwtProvider jwtProvider;

    @Transactional
    public TokenResponseDto reissueTokens(Long userId, String refreshToken) {
        log.info("{ OwnerTokenReissueService } : 토큰 재발급 진입");
        // 사용자가 보유하고 있는 RefreshToken인지
        if (!tokenService.isRefreshTokenExists(userId, refreshToken)) {
            log.info("{ OwnerTokenReissueService } : 유저가 보유하고 있는 토큰이 아님");
            throw BaseException.type(AuthErrorCode.AUTH_INVALID_TOKEN);
        }

        String newAccessToken = jwtProvider.createAccessToken(userId, Role.OWNER);
        String newRefreshToken = jwtProvider.createRefreshToken(userId, Role.OWNER);

        // RTR 정책에 의해 사용자가 보유하고 있는 Refresh Token 업데이트
        tokenService.reissueRefreshTokenByRtrPolicy(userId, newRefreshToken);

        return new TokenResponseDto(newAccessToken, newRefreshToken);
    }
}
package com.menu.auth.service.owner;

import com.menu.auth.dto.request.OwnerLoginRequestDto;
import com.menu.auth.dto.response.OwnerLoginResponseDto;
import com.menu.auth.dto.request.OwnerSignupRequestDto;
import com.menu.auth.service.TokenService;
import com.menu.auth.utils.JwtProvider;
import com.menu.user.domain.User;
import com.menu.user.domain.Role;
import com.menu.user.repository.UserRepository;
import com.menu.user.service.UserFindService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OwnerAuthService {
    private final UserRepository userRepository;
    private final UserFindService userFindService;
    private final TokenService tokenService;
    private final JwtProvider jwtProvider;

    @Transactional
    public Long signup(OwnerSignupRequestDto requestDto) {
        log.info("{ OwnerAuthService } : Owner 회원가입 진입");
        User user = User.of(
                requestDto.name(),
                requestDto.email(),
                Role.OWNER,
                requestDto.socialType(),
                requestDto.socialId(),
                requestDto.profileImageUrl(),
                requestDto.birthday()
        );

        return userRepository.save(user).getId();
    }

    @Transactional
    public OwnerLoginResponseDto login(@Valid OwnerLoginRequestDto requestDto) {
        log.info("{ OwnerAuthService } : Owner 로그인 진입");
        User user = userFindService.findByEmail(requestDto.email());

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtProvider.createRefreshToken(user.getId(), user.getRole());
        tokenService.synchronizeRefreshToken(user.getId(), refreshToken);

        return OwnerLoginResponseDto.fromUser(user, accessToken, refreshToken);
    }

    @Transactional
    public void logout(Long id) {
        tokenService.deleteRefreshTokenByUserId(id);
        log.info("{ OwnerAuthService } : 로그아웃 성공");
    }


}

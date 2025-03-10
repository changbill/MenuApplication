package com.menu.auth.service.user;

import com.menu.auth.dto.request.UserSignupRequestDto;
import com.menu.auth.dto.request.UserLoginRequestDto;
import com.menu.auth.dto.response.UserLoginResponseDto;
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
public class UserAuthService {
    private final UserRepository userRepository;
    private final UserFindService userFindService;
    private final TokenService tokenService;
    private final JwtProvider jwtProvider;

    @Transactional
    public Long signup(UserSignupRequestDto requestDto) {
        log.info("{ UserAuthService } : User 회원가입 진입");
        User user = User.of(
                requestDto.name(),
                requestDto.email(),
                Role.USER,
                requestDto.socialType(),
                requestDto.socialId(),
                requestDto.profileImageUrl(),
                requestDto.birthday()
        );

        return userRepository.save(user).getId();
    }

    @Transactional
    public UserLoginResponseDto login(@Valid UserLoginRequestDto requestDto) {
        log.info("{ UserAuthService } : User 로그인 진입");
        User user = userFindService.findByEmail(requestDto.email());

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtProvider.createRefreshToken(user.getId(), user.getRole());
        tokenService.synchronizeRefreshToken(user.getId(), refreshToken);

        return UserLoginResponseDto.fromUser(user, accessToken, refreshToken);
    }

    @Transactional
    public void logout(Long id) {
        tokenService.deleteRefreshTokenByUserId(id);
        log.info("{ UserAuthService } : 로그아웃 성공");
    }


}

package com.menu.auth.controller.user;

import com.menu.auth.dto.TokenResponseDto;
import com.menu.auth.service.user.UserTokenReissueService;
import com.menu.global.annotation.ExtractPayload;
import com.menu.global.annotation.ExtractToken;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "User 토큰 재발급", description = "UserTokenReissueController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/token/reissue")
public class UserTokenReissueController {
    private final UserTokenReissueService tokenReissueService;

    @PostMapping
    public ResponseEntity<TokenResponseDto> reissueTokens(@ExtractPayload Long id, @ExtractToken String refreshToken) {
        log.info("{ UserTokenReissueController } : 토큰 재발급 진입");
        TokenResponseDto tokenResponseDto = tokenReissueService.reissueTokens(id, refreshToken);
        return ResponseEntity.ok(tokenResponseDto);
    }

}

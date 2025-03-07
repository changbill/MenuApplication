package com.menu.auth.controller;

import com.menu.global.annotation.ExtractPayload;
import com.menu.global.annotation.ExtractToken;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@Tag(name = "토큰 재발급", description = "TokenReissueApiController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token/reissue")
public class TokenReissueController {
    private final TokenReissueService tokenReissueService;

    @PostMapping
    public ResponseEntity<TokenResponseDto> reissueTokens(@ExtractPayload Long id, @ExtractToken String refreshToken) {
        log.info("{ TokenReissueController } : 토큰 재발급 진입");
        TokenResponseDto tokenResponseDto = tokenReissueService.reissueTokens(id, refreshToken);
        return ResponseEntity.ok(tokenResponseDto);
    }

}

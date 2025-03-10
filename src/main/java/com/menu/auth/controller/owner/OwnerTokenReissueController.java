package com.menu.auth.controller.owner;

import com.menu.auth.dto.TokenResponseDto;
import com.menu.auth.service.owner.OwnerTokenReissueService;
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
@Tag(name = "Owner 토큰 재발급", description = "OwnerTokenReissueController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner/token/reissue")
public class OwnerTokenReissueController {
    private final OwnerTokenReissueService ownerTokenReissueService;

    @PostMapping
    public ResponseEntity<TokenResponseDto> reissueTokens(@ExtractPayload Long id, @ExtractToken String refreshToken) {
        log.info("{ OwnerTokenReissueController } : 토큰 재발급 진입");
        TokenResponseDto tokenResponseDto = ownerTokenReissueService.reissueTokens(id, refreshToken);
        return ResponseEntity.ok(tokenResponseDto);
    }

}

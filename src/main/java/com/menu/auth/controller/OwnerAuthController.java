package com.menu.auth.controller;

import com.menu.auth.dto.request.OwnerLoginRequestDto;
import com.menu.auth.dto.response.OwnerLoginResponseDto;
import com.menu.auth.dto.request.OwnerSignupRequestDto;
import com.menu.auth.service.OwnerAuthService;
import com.menu.global.annotation.ExtractPayload;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "OwnerAuth", description = "OwnerAuthController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/owner/auth")
public class OwnerAuthController {
    private final OwnerAuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody OwnerSignupRequestDto requestDto) {
        log.info("{ OwnerAuthController } : owner 회원가입 진입");
        authService.signup(requestDto);
        log.info("{ OwnerAuthService } : owner 회원가입 성공");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<OwnerLoginResponseDto> login(@Valid @RequestBody OwnerLoginRequestDto requestDto) {
        log.info("{ OwnerAuthController } : owner 로그인 진입");
        OwnerLoginResponseDto loginResponseDto = authService.login(requestDto);
        log.info("{ OwnerAuthService } : owner 로그인 성공");
        return ResponseEntity.ok(loginResponseDto);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@ExtractPayload Long id) {
        log.info("{ OwnerAuthController } : owner 로그아웃 진입");
        authService.logout(id);
        log.info("{ OwnerAuthService } : owner 로그아웃 성공");
        return ResponseEntity.ok().build();
    }
}

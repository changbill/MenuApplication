package com.menu.auth.controller.user;

import com.menu.auth.dto.request.UserLoginRequestDto;
import com.menu.auth.dto.request.UserSignupRequestDto;
import com.menu.auth.dto.response.UserLoginResponseDto;
import com.menu.global.annotation.ExtractPayload;
import com.menu.auth.service.user.UserAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "UserAuth", description = "UserAuthController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/auth")
public class UserAuthController {
    private final UserAuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody UserSignupRequestDto requestDto) {
        log.info("{ UserAuthController } : 회원가입 진입");
        authService.signup(requestDto);
        log.info("{ UserAuthService } : 회원가입 성공");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@Valid @RequestBody UserLoginRequestDto requestDto) {
        log.info("{ UserAuthController } : 로그인 진입");
        UserLoginResponseDto loginResponseDto = authService.login(requestDto);
        log.info("{ UserAuthService } : 로그인 성공");
        return ResponseEntity.ok(loginResponseDto);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@ExtractPayload Long id) {
        log.info("{ UserAuthController } : 로그아웃 진입");
        authService.logout(id);
        log.info("{ UserAuthService } : 로그아웃 성공");
        return ResponseEntity.ok().build();
    }
}

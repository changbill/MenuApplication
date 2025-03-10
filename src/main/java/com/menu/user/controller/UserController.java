package com.menu.user.controller;

import com.menu.user.domain.User;
import com.menu.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "User", description = "UserController")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private UserService userService;



    @Operation(
            summary = "토큰 리프레시",
            description = "액세스 토큰이 만료되었을 때 요청해야하는 API. 액세스 토큰이 만료가 되지 않았다면 기존 액세스 토큰을 다시 보내주고, " +
                    "만료가 되었다면 리프레시 토큰이 있는지 확인 후 새로 액세스 토큰을 발급해준다. 리프레시 토큰 기간이 3일 이하로 남았다면, 리프레시 토큰도 새롯 생성한다."
    )
    @GetMapping("/users/refresh")
    public ResponseEntity<UserLoginResponse> refresh(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        return new ResponseEntity(userService.refreshToken(authentication.getName(), request, response), HttpStatus.OK);
    }
}

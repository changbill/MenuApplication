package com.menu.user.controller;

import com.menu.user.domain.Member;
import com.menu.user.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class UserController {
    private MemberService memberService;

    @Operation(
            summary = "회원가입",
            description = "email, password, name, nickname의 정보를 받아 회원가입을 진행한다." +
                    "이후 회원 정보 수정에서 생일, 메모, 공개/비공개 여부를 설정할 수 있다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = MemberJoinResponse.class)))
            }
    )
    @PostMapping("/members/join")
    public ResponseEntity<MemberJoinResponse> join(@RequestBody MemberJoinRequest request) {
        Member member = memberService.join(request.email(), request.password(), request.name(), request.nickname());
        return new ResponseEntity<>(MemberJoinResponse.fromMember(member), HttpStatus.OK);
    }

    @Operation(
            summary = "로그인",
            description = "유저의 email, password를 입력하여 로그인을 진행한다. 이후 JWT 토큰 발급",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 성공 및 JWT 토큰 발급", content = @Content(schema = @Schema(implementation = MemberLoginResponse.class)))
            }
    )
    @PostMapping("/members/login")
    public ResponseEntity<MemberLoginResponse> login(HttpServletRequest request, HttpServletResponse response, @RequestBody MemberLoginRequest memberLoginRequest) {
        return new ResponseEntity<>(memberService.login(request, response, memberLoginRequest.email(), memberLoginRequest.password()), HttpStatus.OK);
    }

    @Operation(
            summary = "로그아웃",
            description = "로그아웃 작업. 레디스 메모리에서 유저 정보 삭제, 리프레시 토큰 정보 삭제, 쿠키 삭제 등의 작업이 이뤄진다."
    )
    @PostMapping("/members/logout")
    public ResponseEntity<Void> logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        memberService.logout(request, response, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "토큰 리프레시",
            description = "액세스 토큰이 만료되었을 때 요청해야하는 API. 액세스 토큰이 만료가 되지 않았다면 기존 액세스 토큰을 다시 보내주고, " +
                    "만료가 되었다면 리프레시 토큰이 있는지 확인 후 새로 액세스 토큰을 발급해준다. 리프레시 토큰 기간이 3일 이하로 남았다면, 리프레시 토큰도 새롯 생성한다."
    )
    @GetMapping("/members/refresh")
    public ResponseEntity<MemberLoginResponse> refresh(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        return new ResponseEntity(memberService.refreshToken(authentication.getName(), request, response), HttpStatus.OK);
    }
}

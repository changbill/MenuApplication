package com.menu.global.security;

import com.menu.auth.exception.AuthErrorCode;
import com.menu.auth.utils.JwtProvider;
import com.menu.global.exception.BaseException;
import com.menu.user.domain.User;
import com.menu.user.service.UserFindService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserFindService userFindService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = jwtProvider.resolveToken(request);
        log.info("{ JwtAuthenticationFilter } : authorizationHeader - "+authorizationHeader);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if (jwtProvider.validateToken(token)) {
                log.info("{ JwtAuthenticationFilter } : 유효한 토큰임을 확인");

                Long userId = jwtProvider.getId(token);
                log.info("{ JwtAuthenticationFilter } : userId - "+userId);

                if(!userFindService.existsById(userId)) {
                    log.info("{ JwtAuthenticationFilter } : 토큰에 추출된 id를 가진 회원 없음");
                    throw BaseException.type(AuthErrorCode.USERID_NOT_FOUND);
                }
                User user = userFindService.findById(userId);

                UserDetailDto userDetailDto = new UserDetailDto(
                        user.getId(),
                        user.getEmail(),
                        user.getName(),
                        user.getRole().getValue()
                );

                CustomUserDetails customUserDetails = new CustomUserDetails(userDetailDto);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(customUserDetails, "", customUserDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            } else {
                log.info("{ JwtAuthenticationFilter } : 유효하지 않은 토큰");
                throw BaseException.type(AuthErrorCode.AUTH_INVALID_TOKEN);
            }
        }

        filterChain.doFilter(request, response);
    }
}

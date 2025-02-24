package com.menu.auth.security.jwt;

import com.menu.auth.utils.AuthorizationExtractor;
import com.menu.member.domain.Member;
import com.menu.member.service.MemberFindService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenStr = AuthorizationExtractor.extractToken(request).orElse(null);
        AuthToken authToken = jwtProvider.convertAuthToken(tokenStr);

        authToken.validateToken();
        Authentication authentication = jwtProvider.getAuthentication(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}

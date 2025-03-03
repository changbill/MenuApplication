package com.menu.auth.config;

import com.menu.auth.dto.UserPrincipal;
import com.menu.auth.handler.OAuth2AuthenticationFailureHandler;
import com.menu.auth.handler.OAuth2AuthenticationSuccessHandler;
import com.menu.auth.repository.OAuth2AuthorizationRequestCookieRepository;
import com.menu.auth.security.jwt.JwtAuthenticationFilter;
import com.menu.auth.security.jwt.JwtProvider;
import com.menu.global.config.properties.AppProperties;
import com.menu.global.config.properties.CorsProperties;
import com.menu.member.repository.MemberRefreshTokenRepository;
import com.menu.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {
    private final AppProperties appProperties;
    private final CorsProperties corsProperties;
    private final JwtProvider jwtProvider;
    private final MemberRefreshTokenRepository memberRefreshTokenRepository;

    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtProvider);
    }

    @Bean
    OAuth2AuthorizationRequestCookieRepository authorizationRequestCookieRepository() {
        return new OAuth2AuthorizationRequestCookieRepository();
    }

    @Bean
    OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(jwtProvider, appProperties, memberRefreshTokenRepository, authorizationRequestCookieRepository());
    }

    @Bean
    OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(authorizationRequestCookieRepository());
    }

    /**
     * Cors 설정
     */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
        config.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
        config.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
        config.addAllowedOriginPattern("*");

        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public UserDetailsService userDetailsService(MemberService memberService) {
        return email -> UserPrincipal.from(memberService.loadUserByEmail(email));
    }
}

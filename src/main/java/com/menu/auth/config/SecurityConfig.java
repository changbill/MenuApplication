package com.menu.auth.config;

import com.menu.auth.exception.RestAuthenticationEntryPoint;
import com.menu.auth.service.CustomOAuth2UserService;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.reactive.config.EnableWebFlux;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final CustomOAuth2UserService oAuth2UserService;
    private final AuthenticationConfig authenticationConfig;

    private static final String[] AUTH_WHITELIST = {
            "/graphiql", "/graphql",
            "/swagger-ui/**", "/api-docs", "/swagger-ui-custom.html",
            "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html"
    };

    private static final String[] OPEN_API_URLS = {
//            "/api/*/users/join",
//            "/api/*/users/login",
//            "/api/*/users/check-email",
//            "/api/*/users/check-nickname",
//            "/api/*/email/**",
//            "/api/*/users/{nickname}",
//            "/api/*/{nickname}/request-profile",
//            "/api/*/{nickname}/count-followers",
//            "/api/*/{nickname}/count-followings"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsConfigurer -> corsConfigurer.configurationSource(authenticationConfig.corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers(OPEN_API_URLS).permitAll()
                                .requestMatchers(AUTH_WHITELIST).permitAll()
                                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                                .requestMatchers("/api/**").authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2Login(configure ->
                        configure.authorizationEndpoint(config ->
                                config.authorizationRequestRepository(authenticationConfig.authorizationRequestCookieRepository())
                                        .baseUri("/oauth2/authorization")
                        )
                                .userInfoEndpoint(config -> config.userService(oAuth2UserService))
                                .redirectionEndpoint(config -> config.baseUri("/*/oauth2/code/*"))
                                .successHandler(authenticationConfig.oAuth2AuthenticationSuccessHandler())
                                .failureHandler(authenticationConfig.oAuth2AuthenticationFailureHandler())
                )
                .exceptionHandling(exceptionManager ->
                        exceptionManager.authenticationEntryPoint(new RestAuthenticationEntryPoint())
                )
                .addFilterBefore(authenticationConfig.jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

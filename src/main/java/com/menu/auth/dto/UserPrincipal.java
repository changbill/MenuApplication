package com.menu.auth.dto;

import com.menu.member.domain.Member;
import com.menu.member.domain.Role;
import com.menu.member.dto.MemberResponse;
import org.apache.catalina.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public record UserPrincipal(
        String email,
        Collection<? extends GrantedAuthority> authorities, // 사용자가 가진 권한(역할) 리스트
        Map<String, Object> attributes      // OAuth2 인증을 통해 얻은 정보
) implements UserDetails, OAuth2User, OidcUser {

    // Owner
    public static UserPrincipal of(
            String email,
            Collection<? extends GrantedAuthority> authorities,
            Map<String, Object> attributes
    ) {
        return new UserPrincipal(
                email,
                authorities,
                attributes
        );
    }

    // member
    public static UserPrincipal of(String email, Map<String, Object> attributes) {
        return new UserPrincipal(
                email,
                Collections.singletonList(new SimpleGrantedAuthority(Role.USER.getValue())),
                attributes
        );
    }

    public static UserPrincipal of(String email) {
        return of(email, Map.of());
    }

    public static UserPrincipal from(MemberResponse dto) {
        return of(dto.email());
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getName() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }
}

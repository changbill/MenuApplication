package com.menu.auth.dto;

import com.menu.member.domain.Role;
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
        String password,
        Collection<? extends GrantedAuthority> authorities,
        Map<String, Object> attributes
) implements UserDetails, OAuth2User, OidcUser {

    public static UserPrincipal of(String email, String password, Map<String, Object> attributes) {
        return new UserPrincipal(
                email,
                password,
                Collections.singletonList(new SimpleGrantedAuthority(Role.USER.getValue())),
                attributes
        );
    }

    public static UserPrincipal of(String email, String password, Collection<? extends GrantedAuthority> authorities) {
        return new UserPrincipal(email, password, authorities, Map.of());
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

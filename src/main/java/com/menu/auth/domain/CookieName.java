package com.menu.auth.domain;

import com.menu.global.utils.EnumStandard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CookieName implements EnumStandard {
    OAUTH2_AUTHORIZATION_REQUEST("oauth2_auth_request"),
    REDIRECT_URI("redirect_uri"),
    MODE("mode"),
    REFRESH_TOKEN("refresh_token"),
    ;

    private final String value;
}

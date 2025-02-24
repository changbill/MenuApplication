package com.menu.auth.domain;

import com.menu.global.utils.EnumStandard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClaimsKey implements EnumStandard {
    EMAIL("email"),
    ROLE("role"),
    ;

    private final String value;
}

package com.menu.member.domain;

import com.menu.global.utils.EnumConverter;
import com.menu.global.utils.EnumStandard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role implements EnumStandard {
    ADMIN("ROLE_ADMIN", "관리자"),
    USER("ROLE_USER", "사용자"),
    GUEST("ROLE_GUEST", "게스트")
    ;

    private final String authority;
    private final String title;

    @Override
    public String getValue() {
        return "";
    }

    @jakarta.persistence.Converter
    public static class RoleConverter extends EnumConverter<Role> {
        public RoleConverter() {
            super(Role.class);
        }
    }
}

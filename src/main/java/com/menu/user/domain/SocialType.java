package com.menu.user.domain;

import com.menu.global.utils.EnumConverter;
import com.menu.global.utils.EnumStandard;
import jakarta.persistence.Converter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum SocialType implements EnumStandard {
    NAVER("NAVER", "네이버"),
    KAKAO("KAKAO", "카카오"),
    GOOGLE("GOOGLE", "구글"),
    ;

    private final String socialType;
    private final String socialTitle;

    public static Optional<SocialType> getSocialType(String name) {
        String upperCaseName = name.toUpperCase();
        for (SocialType socialType : SocialType.values()) {
            if (socialType.getSocialType().equals(upperCaseName)) {
                return Optional.of(socialType);
            }
        }
        return Optional.empty();
    }

    @Override
    public String getValue() {
        return socialType;
    }

    @Converter(autoApply = true)
    public static class SocialTypeConverter extends EnumConverter<SocialType> {
        public SocialTypeConverter() {
            super(SocialType.class);
        }
    }
}

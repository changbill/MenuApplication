package com.menu.global.security.oauth2;

import com.menu.auth.exception.AuthErrorCode;
import com.menu.global.exception.BaseException;
import com.menu.user.domain.SocialType;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(SocialType socialType, Map<String, Object> attributes) {
        switch (socialType) {
            case GOOGLE -> {
                return new GoogleOAuth2UserInfo(attributes);
            }
            case KAKAO -> {
                return new KakaoOAuth2UserInfo(attributes);
            }
            case NAVER -> {
                return new NaverOAuth2UserInfo(attributes);
            }
            default -> throw BaseException.type(AuthErrorCode.INVALID_SOCIAL_TYPE);
        }
    }
}
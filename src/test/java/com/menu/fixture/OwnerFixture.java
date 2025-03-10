package com.menu.fixture;

import com.menu.user.domain.SocialType;
import java.time.LocalDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OwnerFixture {
    CHANGHEON("이창헌", "test@test.com", SocialType.NAVER, "1", "", null),
    ;

    private final String name;
    private final String email;
    private final SocialType socialType;
    private final String socialId;
    private final String profileImageUrl;
    private final LocalDate birthday;
}

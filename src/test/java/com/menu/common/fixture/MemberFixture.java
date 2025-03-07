package com.menu.common.fixture;

import com.menu.user.domain.Member;
import com.menu.user.domain.Role;
import com.menu.user.domain.SocialType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberFixture {
    CHANGHEON("창헌", "test@naver.com",SocialType.NAVER, "id", Role.USER),
    SUNKYOUNG("선경", "abc@naver.com",SocialType.NAVER, "id", Role.USER),
    GABIN("가빈", "def@gmail.com", SocialType.KAKAO, "id", Role.USER),
    JAESIK("재식", "ghi@naver.com", SocialType.KAKAO, "id", Role.USER),
    ;

    private final String username;
    private final String email;
    private final SocialType socialType;
    private final String socialId;
    private final Role role;

    public Member toMember() {
        return Member.of(username, email, Role.USER, socialType, socialId, null, null);
    }
}

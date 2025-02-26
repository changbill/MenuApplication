package com.menu.auth.service;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.menu.auth.domain.OAuth2UserInfo;
import com.menu.auth.domain.OAuth2UserInfoFactory;
import com.menu.auth.dto.UserPrincipal;
import com.menu.auth.exception.AuthErrorCode;
import com.menu.global.exception.BaseException;
import com.menu.member.domain.Member;
import com.menu.member.domain.Role;
import com.menu.member.domain.SocialType;
import com.menu.member.dto.MemberResponse;
import com.menu.member.repository.MemberRepository;
import com.menu.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        SocialType socialType = SocialType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(socialType, oAuth2User.getAttributes());
        if(!StringUtils.hasText(userInfo.getEmail())) {
            throw BaseException.type(AuthErrorCode.EMAIL_NOT_FOUND);
        }

        String socialId = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        Member member = memberRepository.findByEmail(userInfo.getEmail()).orElse(null);
        if(member == null) {
            memberService.memberJoin(userInfo.getName(), userInfo.getEmail(), socialType, socialId, userInfo.getImageUrl());
        }

        return memberRepository.findByEmail(userInfo.getEmail())
                .map(user -> {
                    MemberResponse memberResponse = MemberResponse.from(user);
                    return UserPrincipal.from(memberResponse, oAuth2User.getAttributes());
                })
                .orElseThrow(() ->
                        BaseException.type(AuthErrorCode.EMAIL_NOT_FOUND)
                );
    }
}

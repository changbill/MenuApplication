package com.menu.member.service;

import aj.org.objectweb.asm.commons.Remapper;
import com.menu.global.exception.BaseException;
import com.menu.member.domain.Member;
import com.menu.member.domain.Role;
import com.menu.member.domain.SocialType;
import com.menu.member.dto.MemberResponse;
import com.menu.member.exception.MemberErrorCode;
import com.menu.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponse loadUserByEmail(String email) {
        return memberRepository
                .findByEmail(email)
                .map(MemberResponse::from)
                .orElseThrow(() -> BaseException.type(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public MemberResponse memberJoin(String name, String email, SocialType socialType, String socialId, String imageUrl) {
        Member member = Member.of(name, email, Role.USER, socialType, socialId, imageUrl, null);

        return MemberResponse.from(memberRepository.save(member));
    }
}

package com.menu.member.service;

import com.menu.global.exception.BaseException;
import com.menu.member.domain.Member;
import com.menu.member.exception.MemberErrorCode;
import com.menu.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberFindService {
    private final MemberRepository memberRepository;

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> BaseException.type(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}

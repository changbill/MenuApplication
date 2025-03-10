package com.menu.user.service;

import com.menu.global.exception.BaseException;
import com.menu.user.domain.User;
import com.menu.user.exception.UserErrorCode;
import com.menu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserFindService {
    private final UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> BaseException.type(UserErrorCode.MEMBER_NOT_FOUND));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> BaseException.type(UserErrorCode.MEMBER_NOT_FOUND));
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}

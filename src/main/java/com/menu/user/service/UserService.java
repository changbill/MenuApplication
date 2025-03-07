package com.menu.user.service;

import com.menu.global.exception.BaseException;
import com.menu.user.domain.User;
import com.menu.user.domain.Role;
import com.menu.user.domain.SocialType;
import com.menu.user.dto.UserResponse;
import com.menu.user.exception.UserErrorCode;
import com.menu.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse loadUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .map(UserResponse::from)
                .orElseThrow(() -> BaseException.type(UserErrorCode.MEMBER_NOT_FOUND));
    }

    public UserResponse userJoin(String name, String email, SocialType socialType, String socialId, String imageUrl) {
        User user = User.of(name, email, Role.USER, socialType, socialId, imageUrl, null);

        return UserResponse.from(userRepository.save(user));
    }
}

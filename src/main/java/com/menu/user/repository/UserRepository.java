package com.menu.user.repository;

import com.menu.user.domain.SocialType;
import com.menu.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndSocialType(String email, SocialType socialType);

    boolean existsByEmailAndSocialType(String email, SocialType socialType);
}

package com.menu.member.repository;

import com.menu.member.domain.MemberRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRefreshTokenRepository extends JpaRepository<MemberRefreshToken, Long> {
    MemberRefreshToken findByEmail(String email);
}

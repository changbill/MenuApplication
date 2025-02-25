package com.menu.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_token")
public class MemberRefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_seq")
    private Long refreshTokenSeq;

    @NotNull
    private String email;

    @NotNull
    @Setter
    private String refreshToken;

    public MemberRefreshToken(
            @NotNull String email,
            @NotNull String refreshToken
    ) {
        this.email = email;
        this.refreshToken = refreshToken;
    }


}

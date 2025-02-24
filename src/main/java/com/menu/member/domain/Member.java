package com.menu.member.domain;

import com.menu.global.BaseTimeEntity;
import com.menu.store.domain.Store;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW() where id=?")
@SQLRestriction("deleted_at is NULL")
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String nickname;

    @NotNull
    private LocalDateTime birthDate;

    private Role role;

    private SocialType socialType; // NAVER, GOOGLE

    private String socialId; // 해당 OAuth 의 key(id)

    @Column(length = 500)
    private String profileImageUrl;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Store> stores = new ArrayList<>();

    private Member(
            String name,
            String nickname,
            LocalDateTime birthDate,
            Role role,
            SocialType socialType,
            String socialId,
            String profileImageUrl
    ) {
        this.name = name;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.role = role;
        this.socialType = socialType;
        this.socialId = socialId;
        this.profileImageUrl = profileImageUrl;
    }

    public static Member of(
            String name,
            String nickname,
            LocalDateTime birthDate,
            Role role,
            SocialType socialType,
            String socialId,
            String profileImageUrl
    ) {
        return new Member(name, nickname, birthDate, role, socialType, socialId, profileImageUrl);
    }

    public boolean isOwner() {
        return this.role == Role.OWNER;
    }
}

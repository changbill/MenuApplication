package com.menu.review.domain;

import com.menu.global.BaseTimeEntity;
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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review")
@SQLDelete(sql = "UPDATE review SET deleted_at = NOW() where id=?")
@SQLRestriction("deleted_at is NULL")
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(length = 50)
    private String title;

    @Column(length = 500)
    private String content;

    @Column(length = 500)
    private String photoUrl;

    @NotNull
    private Long rating;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}

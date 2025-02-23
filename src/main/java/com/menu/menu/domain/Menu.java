package com.menu.menu.domain;

import com.menu.global.BaseTimeEntity;
import com.menu.review.domain.Review;
import com.menu.store.domain.Store;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "menu")
@SQLDelete(sql = "UPDATE menu SET deleted_at = NOW() where id=?")   // soft delete 구현
@SQLRestriction("deleted_at is NULL")   // soft delete 된 데이터 제외하고 조회
public class Menu extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private Long price;

    private String photoUrl;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private Menu(String title, Long price, String photoUrl, Store store) {
        this.title = title;
        this.price = price;
        this.photoUrl = photoUrl;
        this.store = store;
    }

    public static Menu of(String title, Long price, String photoUrl, Store store) {
        return new Menu(title, price, photoUrl, store);
    }

    public void update(String title, Long price, String photoUrl) {
        this.title = title;
        this.price = price;
        this.photoUrl = photoUrl;
    }
}

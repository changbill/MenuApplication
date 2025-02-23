package com.menu.menu.repository;


import com.menu.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<List<Menu>> findAllByStoreId(Long storeId);
}

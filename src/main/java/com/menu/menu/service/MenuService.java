package com.menu.menu.service;

import com.menu.menu.domain.Menu;
import com.menu.menu.dto.MenuResponseDto;
import com.menu.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;

    public List<MenuResponseDto> readMenu(Long storeId) {
        List<Menu> menus = menuRepository.readMenuByStoreId(storeId).orElse(new ArrayList<>());

        return menus.stream().map(
                        menu -> MenuResponseDto.builder()
                                .title(menu.getTitle())
                                .price(menu.getPrice())
                                .photoUrl(menu.getPhotoUrl())
                                .build()
                )
                .toList();
    }

    public Long uploadMenu(
            Long ownerId,
            Long storeId,
            MultipartFile image,
            String title,
            Long price
    ) {
        return null;
    }
}

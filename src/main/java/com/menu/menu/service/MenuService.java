package com.menu.menu.service;

import com.menu.file.domain.Directory;
import com.menu.file.service.FileService;
import com.menu.global.exception.BaseException;
import com.menu.menu.domain.Menu;
import com.menu.menu.dto.MenuResponse;
import com.menu.menu.repository.MenuRepository;
import com.menu.store.domain.Store;
import com.menu.store.exception.StoreErrorCode;
import com.menu.store.service.StoreRepository;
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
    private final StoreRepository storeRepository;
    private final FileService fileService;

    public List<MenuResponse> readMenu(Long storeId) {
        List<Menu> menus = menuRepository.readMenuByStoreId(storeId).orElse(new ArrayList<>());

        return menus.stream().map(
                        menu -> MenuResponse.builder()
                                .title(menu.getTitle())
                                .price(menu.getPrice())
                                .photoUrl(menu.getPhotoUrl())
                                .build()
                )
                .toList();
    }

    public Long uploadMenu(
            Long storeId,
            MultipartFile image,
            String title,
            Long price
    ) {
        Store store = storeRepository.findById(storeId).orElseThrow(() -> BaseException.type(StoreErrorCode.STORE_NOT_FOUND));

        String fileUrl = null;
        if(image != null) {
            fileUrl = fileService.uploadImages(Directory.MENU, image);
        }

        Menu menu = Menu.of(title, price, fileUrl, store);
        return menuRepository.save(menu).getId();
    }

    public void updateMenu(
            Long storeId,
            Long menuId,
            MultipartFile image,
            String title,
            Long price
    ) {

    }

    public void deleteMenu(Long storeId, Long menuId) {

    }
}

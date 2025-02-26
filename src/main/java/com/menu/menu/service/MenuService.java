package com.menu.menu.service;

import com.menu.file.domain.Directory;
import com.menu.file.service.FileService;
import com.menu.global.exception.BaseException;
import com.menu.menu.domain.Menu;
import com.menu.menu.dto.MenuResponse;
import com.menu.menu.repository.MenuRepository;
import com.menu.store.domain.Store;
import com.menu.store.exception.StoreErrorCode;
import com.menu.store.service.StoreFindService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final FileService fileService;
    private final MenuFindService menuFindService;
    private final StoreFindService storeFindService;

    @Transactional(readOnly = true)
    public List<MenuResponse> readMenu(Long storeId) {
        List<Menu> menus = menuFindService.findAllByStoreId(storeId);

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
            String email,
            Long storeId,
            MultipartFile image,
            String title,
            Long price
    ) {
        Store store = storeFindService.findById(storeId);
        validateOwner(store, email);

        String fileUrl = null;
        if(image != null)
            fileUrl = fileService.uploadImages(Directory.MENU, image);

        Menu menu = Menu.of(title, price, fileUrl, store);
        return menuRepository.save(menu).getId();
    }

    public void updateMenu(
            String email,
            Long storeId,
            Long menuId,
            MultipartFile image,
            String title,
            Long price
    ) {
        Store store = storeFindService.findById(storeId);
        validateOwner(store, email);

        String fileUrl = null;
        if(image != null)
            fileUrl = fileService.uploadImages(Directory.MENU, image);

        if(store.getPhotoUrl() != null)
            fileService.deleteFiles(store.getPhotoUrl());

        Menu menu = menuFindService.findById(menuId);
        menu.update(title, price, fileUrl);
    }

    public void deleteMenu(String email, Long storeId, Long menuId) {
        Store store = storeFindService.findById(storeId);
        validateOwner(store, email);
        Menu menu = menuFindService.findById(menuId);

        if(store.getPhotoUrl() != null)
            fileService.deleteFiles(store.getPhotoUrl());

        menuRepository.deleteById(menuId);
    }

    private void validateOwner(Store store, String email) {
        if(!store.getOwner().getEmail().equals(email)) {
            throw BaseException.type(StoreErrorCode.USER_IS_NOT_OWNER);
        }
    }
}

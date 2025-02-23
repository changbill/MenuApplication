package com.menu.menu.service;

import com.menu.global.exception.BaseException;
import com.menu.menu.domain.Menu;
import com.menu.menu.exception.MenuErrorCode;
import com.menu.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MenuFindService {
    private final MenuRepository menuRepository;

    public Menu findById(Long id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new BaseException(MenuErrorCode.MENU_NOT_FOUND));
    }

    public List<Menu> findAllByStoreId(Long storeId) {
        return menuRepository.findAllByStoreId(storeId)
                .orElse(new ArrayList<>());
    }
}

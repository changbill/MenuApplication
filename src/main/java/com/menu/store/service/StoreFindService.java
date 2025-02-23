package com.menu.store.service;

import com.menu.global.exception.BaseException;
import com.menu.store.domain.Store;
import com.menu.store.exception.StoreErrorCode;
import com.menu.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StoreFindService {
    private final StoreRepository storeRepository;

    public Store findById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> BaseException.type(StoreErrorCode.STORE_NOT_FOUND));
    }
}

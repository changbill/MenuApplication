package com.menu.menu.controller;

import com.menu.global.annotation.ExtractPayload;
import com.menu.menu.dto.MenuRequest;
import com.menu.menu.dto.MenuResponse;
import com.menu.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menu")
public class MenuApiController {
    private final MenuService menuService;

    @GetMapping("/{storeId}")
    public ResponseEntity<List<MenuResponse>> readMenu(@PathVariable Long storeId) {
        return new ResponseEntity<>(menuService.readMenu(storeId), HttpStatus.OK);
    }

    @PostMapping(value = "/{storeId}", consumes = MULTIPART_FORM_DATA_VALUE)    // multipart/form-data 강제
    public ResponseEntity<Long> uploadMenu(
            @PathVariable Long storeId,
            @RequestPart MultipartFile image,   // 변수명 key로 사용
            @RequestPart MenuRequest request
    ) {
        menuService.uploadMenu(storeId, image, request.title(), request.price());
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{storeId}/{menuId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateMenu(
            @ExtractPayload Long ownerId,
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @RequestPart MultipartFile image,
            @RequestPart MenuRequest request
    ) {
        menuService.updateMenu(ownerId, storeId, menuId, image, request.title(), request.price());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{storeId}/{menuId}")
    public ResponseEntity<Void> deleteMenu(
            @ExtractPayload Long ownerId,
            @PathVariable Long storeId,
            @PathVariable Long menuId
    ) {
        menuService.deleteMenu(ownerId, storeId, menuId);
        return ResponseEntity.ok().build();
    }
}

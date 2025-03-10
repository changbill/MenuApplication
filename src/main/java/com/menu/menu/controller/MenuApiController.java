package com.menu.menu.controller;

import com.menu.global.annotation.ExtractPayload;
import com.menu.menu.dto.MenuRequest;
import com.menu.menu.dto.MenuResponse;
import com.menu.menu.service.MenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Tag(name = "Menu", description = "MenuController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menu")
public class MenuApiController {
    private final MenuService menuService;

    @GetMapping("/{storeId}")
    public ResponseEntity<List<MenuResponse>> readMenu(@PathVariable Long storeId) {
        log.info("{ MenuController } : Menu 조회 진입");
        return new ResponseEntity<>(menuService.readMenu(storeId), HttpStatus.OK);
    }

    @PostMapping(value = "/{storeId}", consumes = MULTIPART_FORM_DATA_VALUE)    // multipart/form-data 강제
    public ResponseEntity<Long> uploadMenu(
            @ExtractPayload String email,
            @PathVariable Long storeId,
            @RequestPart MultipartFile image,   // 변수명 key로 사용
            @RequestPart MenuRequest request
    ) {
        log.info("{ MenuController } : Menu 생성 진입");
        menuService.uploadMenu(email, storeId, image, request.title(), request.price());
        log.info("{ MenuController } : Menu 생성 성공");
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{storeId}/{menuId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateMenu(
            @ExtractPayload String email,
            @PathVariable Long storeId,
            @PathVariable Long menuId,
            @RequestPart MultipartFile image,
            @RequestPart MenuRequest request
    ) {
        log.info("{ MenuController } : Menu 수정 진입");
        menuService.updateMenu(email, storeId, menuId, image, request.title(), request.price());
        log.info("{ MenuController } : Menu 수정 성공");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{storeId}/{menuId}")
    public ResponseEntity<Void> deleteMenu(
            @ExtractPayload String email,
            @PathVariable Long storeId,
            @PathVariable Long menuId
    ) {
        log.info("{ MenuController } : Menu 삭제 진입");
        menuService.deleteMenu(email, storeId, menuId);
        log.info("{ MenuController } : Menu 삭제 성공");
        return ResponseEntity.ok().build();
    }
}

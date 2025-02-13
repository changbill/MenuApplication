package com.menu.menu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menu")
public class MenuApiController {
    private final MenuService menuService;

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuResponse> read(
            @PathVariable("menuId") Long menuId
    ) {
        return new ResponseEntity<>(menuService.read(menuId), HttpStatus.OK);
    }
}

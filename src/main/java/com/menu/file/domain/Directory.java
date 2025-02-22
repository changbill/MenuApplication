package com.menu.file.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Directory {
    MENU("menu"),
    ;

    private final String directory;
}

package com.menu.menu.dto;

import lombok.Builder;

@Builder
public record MenuRequestDto(
        String title,
        Long price
) {
}

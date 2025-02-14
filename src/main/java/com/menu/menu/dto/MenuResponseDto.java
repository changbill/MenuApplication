package com.menu.menu.dto;

import lombok.Builder;

@Builder
public record MenuResponseDto(
        String title,
        Long price,
        String photoUrl
) {
}

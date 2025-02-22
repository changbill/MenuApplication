package com.menu.menu.dto;

import lombok.Builder;

@Builder
public record MenuResponse(
        String title,
        Long price,
        String photoUrl
) {
}

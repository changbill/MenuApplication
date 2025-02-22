package com.menu.menu.dto;

import lombok.Builder;

@Builder
public record MenuRequest(
        String title,
        Long price
) {
}

package com.menu.auth.dto.request;

import com.menu.user.domain.SocialType;
import jakarta.validation.constraints.NotBlank;

public record OwnerLoginRequestDto (
        @NotBlank(message = "이메일은 필수입니다.")
        String email,
        @NotBlank(message = "소셜 타입은 필수입니다.")
        SocialType socialType,
        @NotBlank(message = "소셜 Id는 필수입니다.")
        String socialId
) {}

package com.menu.auth.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.menu.user.domain.SocialType;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record UserSignupRequestDto(
        @NotBlank(message = "이름은 필수입니다.")
        String name,
        @NotBlank(message = "이메일은 필수입니다.")
        String email,
        @NotBlank(message = "소셜 타입은 필수입니다.")
        SocialType socialType,
        @NotBlank(message = "소셜 Id는 필수입니다.")
        String socialId,
        @NotBlank(message = "프로필 이미지는 필수입니다.")
        String profileImageUrl,
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        LocalDate birthday
) {
}

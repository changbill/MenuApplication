package com.menu.member.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.menu.member.domain.Member;
import com.menu.member.domain.Role;
import com.menu.member.domain.SocialType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MemberResponse(
        String name,
        String email,
        Role role,
        SocialType socialType,
        String socialId,
        String profileImageUrl,
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        LocalDate birthday,
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime createdAt,
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime modifiedAt,
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        LocalDateTime deletedAt
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getName(),
                member.getEmail(),
                member.getRole(),
                member.getSocialType(),
                member.getSocialId(),
                member.getProfileImageUrl(),
                member.getBirthday(),
                member.getCreatedAt(),
                member.getModifiedAt(),
                member.getDeletedAt()
        );
    }
}

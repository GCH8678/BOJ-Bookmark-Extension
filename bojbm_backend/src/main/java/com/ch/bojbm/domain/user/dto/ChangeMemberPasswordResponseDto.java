package com.ch.bojbm.domain.user.dto;


import com.ch.bojbm.global.auth.dto.SignUpResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeMemberPasswordResponseDto {
    private String message;

    public static ChangeMemberPasswordResponseDto of(String message) {
        return ChangeMemberPasswordResponseDto.builder()
                .message(message)
                .build();
    }
}

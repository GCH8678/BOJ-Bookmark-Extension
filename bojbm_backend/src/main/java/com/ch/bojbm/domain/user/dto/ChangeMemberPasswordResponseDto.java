package com.ch.bojbm.domain.user.dto;


import com.ch.bojbm.global.auth.dto.SignUpResponseDto;
import lombok.*;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChangeMemberPasswordResponseDto {
    private String message;

    public static ChangeMemberPasswordResponseDto of(String message) {
        return ChangeMemberPasswordResponseDto.builder()
                .message(message)
                .build();
    }
}

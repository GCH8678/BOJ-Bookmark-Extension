package com.ch.bojbm.global.auth.dto;
import lombok.*;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SignUpResponseDto {
    private String message;

    public static SignUpResponseDto of(String message) {
        return SignUpResponseDto.builder()
                .message(message)
                .build();
    }

}

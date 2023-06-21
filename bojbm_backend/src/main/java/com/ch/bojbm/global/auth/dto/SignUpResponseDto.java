package com.ch.bojbm.global.auth.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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

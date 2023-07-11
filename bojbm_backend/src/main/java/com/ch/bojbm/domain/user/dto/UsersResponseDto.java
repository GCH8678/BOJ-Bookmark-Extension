package com.ch.bojbm.domain.user.dto;

import com.ch.bojbm.domain.user.Users;
import lombok.*;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UsersResponseDto {
    private String email;

    public static UsersResponseDto of(Users user) {
        return UsersResponseDto.builder()
                .email(user.getEmail())
                .build();
    }
}

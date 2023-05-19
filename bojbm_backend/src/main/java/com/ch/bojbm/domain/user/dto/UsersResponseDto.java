package com.ch.bojbm.domain.user.dto;

import com.ch.bojbm.domain.user.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersResponseDto {
    private String email;

    public static UsersResponseDto of(Users user) {
        return UsersResponseDto.builder()
                .email(user.getEmail())
                .build();
    }
}

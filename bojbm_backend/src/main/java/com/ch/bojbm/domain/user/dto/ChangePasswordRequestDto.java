package com.ch.bojbm.domain.user.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChangePasswordRequestDto {
    private String authCode;
    private String email;
    private String password;
}

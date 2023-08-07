package com.ch.bojbm.global.auth.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TokenDto {
    private String accessToken; //
    private Long accessTokenExpiration; // 기한
    private String refreshToken; //
    private Long refreshTokenExpiration; // 기한

}

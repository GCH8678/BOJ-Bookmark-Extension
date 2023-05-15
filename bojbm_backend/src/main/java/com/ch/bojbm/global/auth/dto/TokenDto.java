package com.ch.bojbm.global.auth.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    private String grantType; //bearer
    private String accessToken; //
    private Long Expiration; // 기한
}

package com.ch.bojbm.global.auth.dto;

import lombok.*;

import java.time.Duration;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
    private String grantType;
    private String accessToken;
    private Long tokenExpiresIn;
}

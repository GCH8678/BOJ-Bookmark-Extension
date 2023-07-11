package com.ch.bojbm.global.auth.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CodeCheckedDto {
    private Boolean checked;

    public static CodeCheckedDto of(Boolean checked) {
        return CodeCheckedDto.builder()
                .checked(checked)
                .build();
    }

}

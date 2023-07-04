package com.ch.bojbm.global.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodeCheckedDto {
    private Boolean checked;

    public static CodeCheckedDto of(Boolean checked) {
        return CodeCheckedDto.builder()
                .checked(checked)
                .build();
    }

}

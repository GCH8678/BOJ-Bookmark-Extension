package com.ch.bojbm.domain.user;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeMemberPasswordResponseDto {
    private String message;
}

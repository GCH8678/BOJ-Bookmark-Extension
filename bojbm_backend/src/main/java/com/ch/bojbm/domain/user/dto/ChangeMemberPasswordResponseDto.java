package com.ch.bojbm.domain.user.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeMemberPasswordResponseDto {
    private String message;
}

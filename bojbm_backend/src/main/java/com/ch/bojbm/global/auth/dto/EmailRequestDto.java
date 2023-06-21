package com.ch.bojbm.global.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailRequestDto {
    @Email
    @NotBlank(message= "이메일(필수)")
    private String email;
}

package com.ch.bojbm.global.auth;

import com.ch.bojbm.domain.user.dto.UsersRequestDto;
import com.ch.bojbm.domain.user.dto.UsersResponseDto;
import com.ch.bojbm.global.auth.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<UsersResponseDto> signup(@RequestBody UsersRequestDto requestDto) {
        return ResponseEntity.ok(authService.signup(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UsersRequestDto requestDto) {
        return ResponseEntity.ok(authService.login(requestDto));
    }

}

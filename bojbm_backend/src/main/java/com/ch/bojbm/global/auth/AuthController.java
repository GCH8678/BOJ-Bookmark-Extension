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


//    @PostMapping("/signup")
//    public ResponseEntity<UsersResponseDto> signup(@RequestBody UsersRequestDto requestDto) {
//        return ResponseEntity.ok(authService.signup(requestDto));
//    }

    // TODO : 로그인,회원가입 분리 또는 OAuth2등을 이용시 자동으로 가입이 되게 추가 조치 필요
    // 로그인시 없는 회원인 경우 자동적으로 로그인 시도한 이메일과 패스워드로 계정이 생성되도록 함
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UsersRequestDto requestDto) {
        return ResponseEntity.ok(authService.login(requestDto));
    }

}

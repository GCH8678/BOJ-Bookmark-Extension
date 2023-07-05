package com.ch.bojbm.global.auth;

import com.ch.bojbm.domain.mail.EmailService;
import com.ch.bojbm.domain.user.UserService;
import com.ch.bojbm.domain.user.dto.ChangeMemberPasswordResponseDto;
import com.ch.bojbm.domain.user.dto.ChangePasswordRequestDto;
import com.ch.bojbm.domain.user.dto.UsersLoginRequestDto;
import com.ch.bojbm.global.auth.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final EmailService emailService;


    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signup(@RequestBody SignUpRequestDto requestDto) {
        boolean signUpSuccess = authService.signup(requestDto);
        if (!signUpSuccess) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(SignUpResponseDto.of("이미 존재하는 계정입니다."));
        }
        return ResponseEntity.ok(SignUpResponseDto.of("회원가입이 완료되었습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UsersLoginRequestDto loginDto) {
        TokenDto tokenDto = authService.login(loginDto);
        return ResponseEntity.ok(tokenDto);
    }

    // AccessToken 재발급 필요 판단
    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String requestAccessToken) {
        if (!authService.validate(requestAccessToken)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    //토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestHeader("refresh-token") String requestRefreshToken,
                                     @RequestHeader("Authorization") String requestAccessToken) {
        TokenDto reissuedTokenDto = authService.reissue(requestAccessToken, requestRefreshToken);
        
        //토큰 재발급 성공
        if (reissuedTokenDto != null){
            return ResponseEntity.ok(reissuedTokenDto);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorized") String requestAccessToken){
        authService.logout(requestAccessToken);
        return ResponseEntity.ok().build();
    }

    /*
    이메일로 인증번호 전송
     */
    @PostMapping("/signup/email")
    public ResponseEntity<Void> authEmailToSignUp(@RequestBody EmailRequestDto request) {
        if (authService.checkEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        emailService.authEmail(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/email")
    public ResponseEntity<Void> authEmailToFindPassword(@RequestBody EmailRequestDto request) {
        if (authService.checkEmail(request.getEmail())) {
            emailService.authEmail(request);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @GetMapping(value = {"/signup/code", "/password/code"})
    public ResponseEntity<CodeCheckedDto> checkCode(@RequestParam(value = "authCode") String authCode, @RequestParam(value = "email") String email) {
        boolean codeCheck = authService.checkCode(authCode, email);
        if (!codeCheck) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CodeCheckedDto.of(false));
        }
        return ResponseEntity.ok(CodeCheckedDto.of(true));
    }

    @PostMapping("/password/change")
    public ResponseEntity<ChangeMemberPasswordResponseDto> setMemberPassword(@RequestBody ChangePasswordRequestDto request) {
        try {
            return ResponseEntity.ok(userService.changeMemberPassword(request));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


}

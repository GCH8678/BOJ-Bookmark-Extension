package com.ch.bojbm.global.auth;

import com.ch.bojbm.domain.mail.EmailService;
import com.ch.bojbm.domain.user.UserService;
import com.ch.bojbm.domain.user.dto.ChangeMemberPasswordResponseDto;
import com.ch.bojbm.domain.user.dto.ChangePasswordRequestDto;
import com.ch.bojbm.domain.user.dto.UsersRequestDto;
import com.ch.bojbm.global.auth.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<TokenDto> login(@RequestBody UsersRequestDto requestDto) {
        return ResponseEntity.ok(authService.login(requestDto));
    }

    /*
    이메일로 인증번호 전송
     */
    @PostMapping("/signup/email")
    public ResponseEntity<Void> authEmailToSignUp(@RequestBody EmailRequestDto request){
        if(authService.checkEmail(request.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        emailService.authEmail(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/email")
    public ResponseEntity<Void> authEmailToFindPassword(@RequestBody EmailRequestDto request){
        if(authService.checkEmail(request.getEmail())){
            emailService.authEmail(request);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @GetMapping(value={"/signup/code","/password/code"})
    public ResponseEntity<CodeCheckedDto> checkCode(@RequestParam(value="authCode") String authCode, @RequestParam(value="email") String email){
        boolean codeCheck = authService.checkCode(authCode,email);
        if(!codeCheck){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CodeCheckedDto.of(false));
        }
        return ResponseEntity.ok(CodeCheckedDto.of(true));
    }

    @PostMapping("/password/change")
    public ResponseEntity<ChangeMemberPasswordResponseDto> setMemberPassword(@RequestBody ChangePasswordRequestDto request) {
        try {
            return ResponseEntity.ok(userService.changeMemberPassword(request));
        }catch(IllegalArgumentException exception){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



}

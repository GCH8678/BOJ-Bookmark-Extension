package com.ch.bojbm.global.auth;

import com.ch.bojbm.domain.user.UsersRepository;
import com.ch.bojbm.domain.user.Users;
import com.ch.bojbm.domain.user.dto.UsersRequestDto;
import com.ch.bojbm.global.auth.dto.SignUpRequestDto;
import com.ch.bojbm.global.auth.dto.SignUpResponseDto;
import com.ch.bojbm.global.auth.dto.TokenDto;
import com.ch.bojbm.global.auth.token.TokenProvider;
import com.ch.bojbm.global.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UsersRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RedisUtil redisUtil;

    public boolean signup(SignUpRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            return false;
        }

        Users users = requestDto.toUsersEntity(passwordEncoder);
        memberRepository.save(users);
        return true;
    }

    public TokenDto login(UsersRequestDto requestDto) {

        if(!memberRepository.existsByEmail(requestDto.getEmail())){
            throw new RuntimeException("존재하지 않는 계정입니다.");
        }

        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return tokenProvider.generateTokenDto(authentication);
    }

    public boolean checkEmail(String email) {
        return memberRepository.existsByEmail(email);
    }


    public boolean checkCode(String authCode,String email) {
        boolean checkedCode = email.equals(redisUtil.getData(authCode));
        if(checkedCode){
            redisUtil.deleteData(authCode);
            return true;
        }
        return false;
    }
}

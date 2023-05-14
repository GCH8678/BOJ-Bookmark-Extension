package com.ch.bojbm.global.auth;

import com.ch.bojbm.domain.user.UsersRepository;
import com.ch.bojbm.domain.user.Users;
import com.ch.bojbm.domain.user.dto.UsersRequestDto;
import com.ch.bojbm.domain.user.dto.UsersResponseDto;
import com.ch.bojbm.global.auth.dto.TokenDto;
import com.ch.bojbm.global.auth.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final AuthenticationManagerBuilder managerBuilder;
    private final UsersRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public UsersResponseDto signup(UsersRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Users users = requestDto.toEntity(passwordEncoder);
        return UsersResponseDto.of(memberRepository.save(users));
    }

    public TokenDto login(UsersRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();

        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        return tokenProvider.generateTokenDto(authentication);
    }

}

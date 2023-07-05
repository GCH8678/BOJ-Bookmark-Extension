package com.ch.bojbm.global.auth;

import com.ch.bojbm.domain.user.UsersRepository;
import com.ch.bojbm.domain.user.Users;
import com.ch.bojbm.domain.user.dto.UsersLoginRequestDto;
import com.ch.bojbm.global.auth.dto.SignUpRequestDto;
import com.ch.bojbm.global.auth.dto.TokenDto;
import com.ch.bojbm.global.auth.tokenProvider.JwtTokenProvider;
import com.ch.bojbm.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    private final String SERVER = "Server";

    @Transactional
    public boolean signup(SignUpRequestDto requestDto) {
        if (usersRepository.existsByEmail(requestDto.getEmail())) {
            return false;
        }

        Users users = requestDto.toUsersEntity(passwordEncoder);
        usersRepository.save(users);
        return true;
    }

    // 로그인 : 인증 정보 저장 및 Bearer 토큰 발급
    @Transactional
    public TokenDto login(UsersLoginRequestDto requestDto) {

        if(!usersRepository.existsByEmail(requestDto.getEmail())){
            throw new RuntimeException("존재하지 않는 계정입니다.");
        }

        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthenticationToken();

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return generateToken(SERVER,authentication.getName(),getAuthorities(authentication));
    }
    
    // AccessToken 이 만료되었는지 검사
    public boolean validate(String requestAccessTokenInHeader){
        String requestAccessToken = resolveToken(requestAccessTokenInHeader);
        return jwtTokenProvider.validateAccessTokenOnlyExpired(requestAccessToken); // true면 재발급
    }

    //토큰 재발급 : validate 메서드가 true를 반환할때만
    @Transactional
    public TokenDto reissue(String requestAccessTokenInHeader, String requestRefreshToken){
        String requestAccessToken = resolveToken(requestAccessTokenInHeader);

        Authentication authentication = jwtTokenProvider.getAuthentication(requestAccessToken);
        String principal = getPrincipal(requestAccessToken);

        //Redis에 저장되어 있는 RT 삭제
        String refreshTokenInRedis = redisService.getData("RT("+SERVER+"):"+principal);
        if (refreshTokenInRedis ==null){ // Redis에 RefreshToken이 없는 경우
            return null; // 재로그인 요청
        }

        if(!jwtTokenProvider.validateRefreshToken(requestRefreshToken) || !refreshTokenInRedis.equals(requestRefreshToken)){
            redisService.deleteData("RT(" + SERVER + "):" + principal); // 탈취 가능성이 있으므로 삭제한다
            return null; // 재로그인 요청
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String authorities = getAuthorities(authentication);

        //토큰 재발급 및 Redis 업데이트
        redisService.deleteData("RT(" + SERVER + "):" + principal);
        TokenDto tokenDto = jwtTokenProvider.createToken(principal, authorities);
        saveRefreshToken(SERVER, principal, tokenDto.getRefreshToken());
        return tokenDto;
    }
    



    public boolean checkEmail(String email) {
        return usersRepository.existsByEmail(email);
    }

    @Transactional
    public TokenDto generateToken(String provider, String email, String authorities){
        //Refresh Token이 이미 있는 경우
        if(redisService.getData("RT("+provider+"):"+email) !=null){
            redisService.deleteData("RT("+provider+"):"+email); //삭제
        }
        TokenDto tokenDto = jwtTokenProvider.createToken(email, authorities);
        saveRefreshToken(provider, email, tokenDto.getRefreshToken());
        return tokenDto;
    }

    // RefreshToken을 Redis에 저장
    @Transactional
    public void saveRefreshToken(String provider, String principal, String refreshToken) {
        redisService.setDataExpire("RT("+provider+"):"+principal,refreshToken,jwtTokenProvider.getTokenExpirationTime(refreshToken));
    }


    // "Bearer {AccessToken}"에서 {AccessToken} 추출
    public String resolveToken(String requestAccessTokenInHeader) {
        if (requestAccessTokenInHeader != null && requestAccessTokenInHeader.startsWith("Bearer ")) {
            return requestAccessTokenInHeader.substring(7);
        }
        return null;
    }
    
    // 권한 이름 가져오기
    private String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    // AccessToken으로 부터 principal 추출
    public String getPrincipal(String requestAccessToken) {
        return jwtTokenProvider.getAuthentication(requestAccessToken).getName();
    }
    
    // 가입, 비밀번호 찾기 인증 코드 검증
    public boolean checkCode(String authCode,String email) {
        boolean checkedCode = email.equals(redisService.getData(authCode));
        if(checkedCode){
            redisService.deleteData(authCode);
            return true;
        }
        return false;
    }

    @Transactional
    public void logout(String requestAccessTokenInHeader){
        String requestAccessToken = resolveToken(requestAccessTokenInHeader);
        String principal = getPrincipal(requestAccessToken);

        //Redis에 저장된 RefreshToken 삭제
        String refreshTokenInRedis = redisService.getData("RT(" + SERVER + "):" + principal);
        if (refreshTokenInRedis != null) {
            redisService.deleteData("RT(" + SERVER + "):" + principal);
        }
        
        //Redis에 로그아웃 처리한 AccessToken 저장
        long expiration = jwtTokenProvider.getTokenExpirationTime(requestAccessToken) - new Date().getTime();
        redisService.setDataExpire(requestAccessToken,"logout",expiration);

    }

}

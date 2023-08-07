package com.ch.bojbm.global.auth.tokenProvider;

import com.ch.bojbm.domain.user.UserDetailServiceImpl;
import com.ch.bojbm.domain.user.UserDetailsImpl;
import com.ch.bojbm.global.auth.dto.TokenDto;
import com.ch.bojbm.global.redis.RedisService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.*;

@Slf4j
@Component
@Transactional(readOnly = true)
public class JwtTokenProvider implements InitializingBean { // Secret Key 값 사용전 미리 초기화 하기 위해 상속 받음

    private final UserDetailServiceImpl userDetailService;
    private final RedisService redisService;

    private static final String AUTHORITIES_KEY = "role";
    private static final String EMAIL_KEY = "email";

    private final Long accessTokenExpireTimeInMilliseconds;
    private final Long refreshTokenExpireTimeInMilliseconds;

    private final String secretKey;
    private static Key signingKey;


    public JwtTokenProvider(
            UserDetailServiceImpl userDetailService,
            RedisService redisService,
            @Value("${jwt.secretKey}") String secretKey,
            @Value("${jwt.access.expiration}") Long accessTokenExpireTimeInMilliseconds,
            @Value("${jwt.refresh.expiration}") Long refreshTokenExpireTimeInMilliseconds
            ) {
        this.userDetailService = userDetailService;
        this.redisService = redisService;
        this.secretKey = secretKey;

        this.accessTokenExpireTimeInMilliseconds = accessTokenExpireTimeInMilliseconds;
        this.refreshTokenExpireTimeInMilliseconds = refreshTokenExpireTimeInMilliseconds;

    }

    //시크릿 키 설정

    @Override
    public void afterPropertiesSet(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성
    @Transactional
    public TokenDto createToken(String email,String authorities) {


        Long now = System.currentTimeMillis();
        Date accessTokenExpiration = new Date(now+accessTokenExpireTimeInMilliseconds);
        Date refreshTokenExpiration = new Date(now+refreshTokenExpireTimeInMilliseconds);

        String accessToken = Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setHeaderParam("alg","HS512")
                .setExpiration(accessTokenExpiration)
                .setSubject("access-token")
                .claim(EMAIL_KEY,email)
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setHeaderParam("alg","HS512")
                .setExpiration(refreshTokenExpiration)
                .setSubject("refresh-token")
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();

        return TokenDto.builder()
                .accessToken(accessToken)
                .accessTokenExpiration(accessTokenExpiration.getTime())
                .refreshToken(refreshToken)
                .refreshTokenExpiration(refreshTokenExpiration.getTime())
                .build();
    }

    // 토큰으로부터 정보 추출
    private Claims getClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(signingKey).build()
                    .parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Authentication getAuthentication(String accessToken) {
        String email = getClaims(accessToken).get(EMAIL_KEY).toString();
        UserDetailsImpl userDetailsImpl = userDetailService.loadUserByUsername(email);

        return new UsernamePasswordAuthenticationToken(userDetailsImpl, "", userDetailsImpl.getAuthorities());
    }

    public long getTokenExpirationTime(String token){
        return getClaims(token).getExpiration().getTime();
    }


    //토큰 검증

    public boolean validateRefreshToken(String refreshToken){
        try{
            if (redisService.getData(refreshToken).equals("delete")){ // 회원 탈퇴 했을 경우
                return false;
            }
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(refreshToken);
            return true;
        } catch (SignatureException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("존재하지 않는 JWT 클레임입니다.");
        } catch (NullPointerException e){
            log.error("존재하지 않는 JWT 토큰입니다.");
        }
        return false;
    }
    //Filter 에서 사용
    public boolean validateAccessToken(String accessToken) {
        try {
            if(redisService.getData(accessToken) != null    // NullPointerException 방지
            && redisService.getData(accessToken).equals("logout")){ // 로그아웃
                return false;
            }
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(accessToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
    
    // 재발급 검증 API에서 사용
    public boolean validateAccessTokenOnlyExpired(String accessToken){
        try {
            return getClaims(accessToken)
                    .getExpiration()
                    .before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }






}

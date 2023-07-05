package com.ch.bojbm.global.config;

import com.ch.bojbm.global.auth.filter.JwtAuthenticationFilter;
import com.ch.bojbm.global.auth.handler.JwtAccessDeniedHandler;
import com.ch.bojbm.global.auth.handler.JwtAuthenticationEntryPoint;
import com.ch.bojbm.global.auth.tokenProvider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Component
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        // DB 저장 전 사용할 암호화 인코더
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        // 접근 제어 목록의 예외 URL 설정 => 아래 코드는 정적 리소스에 대한 예외 처리
        return (web)
                -> web
                .ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // jwt 토큰 사용을 위한 설정
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 예외처리 설정
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/api/auth/**"
                )
                .permitAll()
//                .requestMatchers("/api/admin/**")
//                .hasRole("ADMIN")
                .anyRequest().authenticated()

                .and()
                .headers()
                .frameOptions().sameOrigin();
        ;
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}

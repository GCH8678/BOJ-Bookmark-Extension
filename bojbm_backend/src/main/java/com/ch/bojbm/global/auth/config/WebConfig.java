package com.ch.bojbm.global.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {  // 전역적으로 CORS 관련 필터 적용
    private final long MAX_AGE_SECS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 프로그램에서 제공하는 URL
                .allowedOrigins("*")    //외부에서 들어오는 모둔 url 을 허용
                .allowedMethods("*"/*"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"*/)    //허용되는 Method
                .allowedHeaders("*")    //허용되는 헤더
                .allowCredentials(false)    //자격증명 허용 <- 쿠키 요청 허용에 관한 것 (다른 도메인 서버에 인증하는 경우에만 사용, true 적용시 보안상 이슈 발생 가능)
                .maxAge(MAX_AGE_SECS);   //허용 시간
    }
}

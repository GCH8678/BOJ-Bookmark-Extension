package com.ch.bojbm.ControllerTest;

import com.ch.bojbm.domain.mail.EmailService;
import com.ch.bojbm.domain.user.UserService;
import com.ch.bojbm.domain.user.dto.ChangeMemberPasswordResponseDto;
import com.ch.bojbm.global.auth.AuthController;
import com.ch.bojbm.global.auth.AuthService;
import com.ch.bojbm.global.auth.dto.TokenDto;
import com.ch.bojbm.global.auth.filter.JwtAuthenticationFilter;
import com.ch.bojbm.global.config.WebSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class})
@WebMvcTest(controllers = AuthController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfig.class),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
@MockBean(JpaMetamodelMappingContext.class)
@ActiveProfiles("local")
class AuthControllerTest {
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @MockBean
    private AuthService authService;
    @MockBean
    private UserService userService;
    @MockBean
    private EmailService emailService;


    @Test
    @DisplayName("[API][POST] 회원가입 - 성공")
    void signupSuccess() throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("email", "test@test.com");
        map.put("password", "1234");

        given(authService.signup(any())).willReturn(true);

        ResultActions act = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(map))
        );

        act.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("auth/signup-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("가입 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("가입 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("회원가입 완료 메시지")
                        )

                ));

    }

    @Test
    @DisplayName("[API][POST] 회원가입 - 실패(이미 회원 존재)")
    void signupFail() throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("email", "test@test.com");
        map.put("password", "1234");

        given(authService.signup(any())).willReturn(false);

        ResultActions act = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(map))
        );

        act.andExpect(status().isConflict())
                .andDo(print())
                .andDo(document("auth/signup-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("가입 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("가입 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("회원가입 실패 메시지")
                        )

                ));
    }


    @Test
    @DisplayName("[API][POST] 로그인 - 성공")
    void loginSuccess() throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("email", "test@test.com");
        map.put("password", "1234");

        long now = (new Date()).getTime();
        Date Expiration = new Date(now + 1000 * 60 * 60 * 24); //24시간
        TokenDto generatedTokenDto = TokenDto.builder()
                .accessToken("{JWT Access Token}")
                .accessTokenExpiration(Expiration.getTime()) //24시간
                .refreshToken("{JWT Refresh Token}")
                .refreshTokenExpiration(Expiration.getTime()*28) // 한달
                .build();

        given(authService.login(any())).willReturn(generatedTokenDto);

        ResultActions act = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(map))
                .characterEncoding("UTF-8")
        );

        act.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("auth/login-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("가입 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("가입 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING)
                                        .description("JWT ACCESS 토큰"),
                                fieldWithPath("accessTokenExpiration").type(JsonFieldType.NUMBER)
                                        .description("JWT Access 토큰 만료 기간"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                        .description("JWT Refresh 토큰"),
                                fieldWithPath("refreshTokenExpiration").type(JsonFieldType.NUMBER)
                                        .description("JWT Refresh 토큰 만료 기간")
                        )

                ));
    }

    @Test
    @DisplayName("[API][POST] 가입 인증 이메일 발송 성공")
    void authEmailToSignUpSuccess() throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("email", "test@test.com");

        given(authService.checkEmail(any())).willReturn(false);
        ResultActions act = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup/email")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(map))
                .characterEncoding("UTF-8")
        );

        act.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("auth/signup-auth-email-success",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING)
                                                .description("가입 인증 이메일")
                                )
                        )
                );
    }

    @Test
    @DisplayName("[API][POST] 가입 인증 이메일 발송 실패 - 이미 존재하는 회원 이메일")
    void authEmailToSignUpFail() throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("email", "test@test.com");

        given(authService.checkEmail(any())).willReturn(true);
        ResultActions act = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup/email")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(map))
                .characterEncoding("UTF-8")
        );

        act.andExpect(status().isConflict())
                .andDo(print())
                .andDo(document("auth/signup-auth-email-fail",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING)
                                                .description("가입 인증 이메일")
                                )
                        )
                );
    }

    @Test
    @DisplayName("[API][POST] 비밀번호 변경시 인증 이메일 발송 성공")
    void authEmailToFindPasswordSuccess() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("email", "test@test.com");

        given(authService.checkEmail(any())).willReturn(true);
        ResultActions act = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/password/email")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(map))
                .characterEncoding("UTF-8")
        );

        act.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("auth/find-password-auth-email-success",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING)
                                                .description("비밀번호 변경 인증 이메일")
                                )
                        )
                );
    }

    @Test
    @DisplayName("[API][POST] 비밀번호 변경시 인증 이메일 발송 실패")
    void authEmailToFindPasswordFail() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("email", "test@test.com");

        given(authService.checkEmail(any())).willReturn(false);
        ResultActions act = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/password/email")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(map))
                .characterEncoding("UTF-8")
        );

        act.andExpect(status().isNotFound())
                .andDo(print())
                .andDo(document("auth/find-password-auth-email-fail",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING)
                                                .description("비밀번호 변경 인증 이메일")
                                )
                        )
                );
    }

    @Test
    @DisplayName("[API][GET] 가입 이메일 인증 코드 확인 - 성공")
    void checkSignUpCodeSuccess() throws Exception {

        given(authService.checkCode(any(), any())).willReturn(true);
        ResultActions act = mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/signup/code")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("email", "test@test.com")
                .param("authCode", "{Auth Code}")
                .characterEncoding("UTF-8")
        );
        act.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("auth/check-signup-code-success",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                queryParameters(
                                        parameterWithName("authCode").description("인증 코드 번호"),
                                        parameterWithName("email").description("인증 이메일")
                                ),
                                responseFields(
                                        fieldWithPath("checked").type(JsonFieldType.BOOLEAN)
                                                .description("인증 성공 여부")
                                )
                        )
                );
    }

    @Test
    @DisplayName("[API][GET] 가입 이메일 인증 코드 확인 - 실패")
    void checkSignUpCodeFail() throws Exception {

        given(authService.checkCode(any(), any())).willReturn(false);
        ResultActions act = mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/signup/code")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("email", "test@test.com")
                .param("authCode", "{Auth Code}")
                .characterEncoding("UTF-8")
        );
        act.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("auth/check-signup-code-fail",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                queryParameters(
                                        parameterWithName("authCode").description("인증 코드 번호"),
                                        parameterWithName("email").description("인증 이메일")
                                ),
                                responseFields(
                                        fieldWithPath("checked").type(JsonFieldType.BOOLEAN)
                                                .description("인증 성공 여부")
                                )
                        )
                );
    }

    @Test
    @DisplayName("[API][GET] 비밀번호 찾기 인증 코드 확인 - 성공")
    void checkFindPasswordCodeSuccess() throws Exception {


        given(authService.checkCode(any(), any())).willReturn(true);
        ResultActions act = mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/password/code")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("email", "test@test.com")
                .param("authCode", "{Auth Code}")
                .characterEncoding("UTF-8")
        );
        act.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("auth/check-find-password-code-success",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                queryParameters(
                                        parameterWithName("authCode").description("인증 코드 번호"),
                                        parameterWithName("email").description("인증 이메일")
                                ),
                                responseFields(
                                        fieldWithPath("checked").type(JsonFieldType.BOOLEAN)
                                                .description("인증 성공 여부")
                                )
                        )
                );
    }

    @Test
    @DisplayName("[API][GET] 비밀번호 찾기 인증 코드 확인 - 실패")
    void checkFindPasswordCodeFail() throws Exception {


        given(authService.checkCode(any(), any())).willReturn(false);
        ResultActions act = mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/password/code")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("email", "test@test.com")
                .param("authCode", "{Auth Code}")
                .characterEncoding("UTF-8")
        );
        act.andExpect(status().isBadRequest())
                .andDo(print())
                .andDo(document("auth/check-find-password-code-fail",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                queryParameters(
                                        parameterWithName("authCode").description("인증 코드 번호"),
                                        parameterWithName("email").description("인증 이메일")
                                ),
                                responseFields(
                                        fieldWithPath("checked").type(JsonFieldType.BOOLEAN)
                                                .description("인증 성공 여부")
                                )
                        )
                );
    }


    @Test
    @DisplayName("[API][Post] 비밀번호 변경 - 성공")
    void setMemberPasswordSuccess() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("authCode", "{Auth Code}");
        map.put("email", "test@test.com");
        map.put("password","4321");

        given(userService.changeMemberPassword(any())).willReturn(ChangeMemberPasswordResponseDto.of("비밀번호가 변경되었습니다."));
        ResultActions act = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/password/change")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(map))
                .characterEncoding("UTF-8")
        );
        act.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("auth/set-member-password-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("authCode").type(JsonFieldType.STRING)
                                                .description("인증 코드 번호"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("유저 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("변경 하려는 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                        .description("응답 성공 메시지")
                        )
                ));
    }
    @Test
    @DisplayName("[API][Post] 비밀번호 변경 - 실패 (잘못된 인증코드 나 다양한 이유로)")
    void setMemberPasswordFail() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("authCode", "{Auth Code}");
        map.put("email", "test@test.com");
        map.put("password","4321");

        given(userService.changeMemberPassword(any())).willThrow(new IllegalArgumentException("잘못된 요청 입니다."));
        ResultActions act = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/password/change")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(map))
                .characterEncoding("UTF-8")
        );
        act.andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(document("auth/set-member-password-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("authCode").type(JsonFieldType.STRING)
                                        .description("인증 코드 번호"),
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                        .description("유저 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                        .description("변경 하려는 비밀번호")
                        )
                ));
    }
}
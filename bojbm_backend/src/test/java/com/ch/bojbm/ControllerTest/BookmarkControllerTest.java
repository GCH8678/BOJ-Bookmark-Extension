package com.ch.bojbm.ControllerTest;


import com.ch.bojbm.domain.bookmark.BookmarkController;
import com.ch.bojbm.domain.bookmark.BookmarkService;
import com.ch.bojbm.domain.bookmark.dto.BookmarkInListResponseDto;
import com.ch.bojbm.domain.bookmark.dto.BookmarkListResponseDto;
import com.ch.bojbm.domain.bookmark.dto.TodayProblemDto;
import com.ch.bojbm.domain.bookmark.dto.TodayProblemsResponseDto;
import com.ch.bojbm.domain.notification.NotificationService;
import com.ch.bojbm.global.auth.filter.JwtAuthenticationFilter;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class})
@WebMvcTest(controllers = BookmarkController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)})
@MockBean(JpaMetamodelMappingContext.class)
@ActiveProfiles("local")
class BookmarkControllerTest {

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
    private BookmarkService bookmarkService;
    @MockBean
    private NotificationService notificationService;


    @Test
    @DisplayName("[API][GET] 모든 북마크 리스트 조회")
    @WithMockUser(username = "test@test.com", password = "1234", roles = {"USER", "ADMIN"})
    void getBookmarkList() throws Exception {
        //given
        BookmarkInListResponseDto bookmark1 = BookmarkInListResponseDto.builder()
                .notificationDate(LocalDate.now())
                .problemNum(1)
                .build();
        BookmarkInListResponseDto bookmark2 = BookmarkInListResponseDto.builder()
                .notificationDate(LocalDate.now())
                .problemNum(2)
                .build();
        List<BookmarkInListResponseDto> bookmarkList = List.of(bookmark1, bookmark2);

        given(bookmarkService.getAllBookmarks(any()))
                .willReturn(BookmarkListResponseDto.builder().bookmarkList(bookmarkList).build());

        //when & then
        ResultActions act = mockMvc.perform(MockMvcRequestBuilders.get("/api/bookmark/list")
                .header("Authentication", "Bearer {Jwt Access Token}")
                .accept(MediaType.APPLICATION_JSON));
        // TODO : Service 계층 단위테스트 필요, given메소드를 통해 MockBean으로 부터 Body에 넣을 값을 받기 때문에 추가 테스트코드 작성 필요
        act.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("bookmark/list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("bookmarkList").type(JsonFieldType.ARRAY).description("모든 북마크 목록(빈 배열도 반환)"),
                                fieldWithPath("bookmarkList.[].notificationDate").type(JsonFieldType.ARRAY).description("알람 일자"),
                                fieldWithPath("bookmarkList.[].problemNum").type(JsonFieldType.NUMBER).description("문제 번호")
                        )
                ));
    }


    @Test
    @DisplayName("[API][GET] 오늘 풀어야할 문제 목록 조회")
    @WithMockUser(username = "test@test.com", password = "1234", roles = {"USER", "ADMIN"})
    void getTodayProblemList() throws Exception {
        //given
        TodayProblemDto problem1 = TodayProblemDto.builder()
                .problemTitle("1번 문제")
                .problemNum(1000)
                .build();
        TodayProblemDto problem2 = TodayProblemDto.builder()
                .problemTitle("2번 문제")
                .problemNum(2345)
                .build();
        List<TodayProblemDto> problemList = List.of(problem1, problem2);
        given(bookmarkService.getTodayProblemList(any()))
                .willReturn(TodayProblemsResponseDto.builder().problemList(problemList).build());
        //when
        ResultActions act = mockMvc.perform(MockMvcRequestBuilders.get("/api/bookmark/list/today")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authentication", "{Jwt Access Token}"));
        act.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("bookmark/list-today",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("problemList").type(JsonFieldType.ARRAY).description("오늘 풀 북마크 문제 목록 (빈 배열도 반환)"),
                                fieldWithPath("problemList.[].problemNum").type(JsonFieldType.NUMBER).description("문제 번호"),
                                fieldWithPath("problemList.[].problemTitle").type(JsonFieldType.STRING).description("문제 제목")
                        )
                ));
        //then
    }

    @Test
    @DisplayName("[API][POST] 북마크 추가")
    @WithMockUser(username = "test@test.com", password = "1234", roles = {"USER", "ADMIN"})
    void addBookmark() throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("problemId", 1107);
        map.put("problemTitle", "리모컨");
        map.put("afterDay", 7);

        ResultActions act = mockMvc.perform(MockMvcRequestBuilders.post("/api/bookmark")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(map))
                .header("Authentication", "Bearer {Jwt Access Token}"));
        // 성공적으로 추가
        act.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("bookmark/add",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("problemId").type(JsonFieldType.NUMBER)
                                        .description("문제 번호"),
                                fieldWithPath("afterDay").type(JsonFieldType.NUMBER)
                                        .description("n일 뒤 알림"),
                                fieldWithPath("problemTitle").type(JsonFieldType.STRING)
                                        .description("문제 제목")
                        )
                ));
    }

    @Test
    @DisplayName("[API][PUT] 북마크 수정")
    @WithMockUser(username = "test@test.com", password = "1234", roles = {"USER", "ADMIN"})
    void updateBookmark() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("problemId", 1107);
        map.put("afterDay", 7);

        ResultActions act = mockMvc.perform(MockMvcRequestBuilders.put("/api/bookmark")
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(map))
                .header("Authentication", "Bearer {Jwt Access Token}"));
        act.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("bookmark/update",
                        preprocessRequest(),
                        preprocessResponse(),
                        requestFields(
                                fieldWithPath("problemId").type(JsonFieldType.NUMBER)
                                        .description("문제 번호"),
                                fieldWithPath("afterDay").type(JsonFieldType.NUMBER)
                                        .description("n일 뒤 알림")

                        )
                ));
    }

    @Test
    @DisplayName("[API][GET] 북마크 여부 확인")
    @WithMockUser(username = "test@test.com", password = "1234", roles = {"USER", "ADMIN"})
    void checkBookmark() throws Exception {

        Integer problemNum = 1107;
        given(bookmarkService.checkBookmark(any(), eq(problemNum))).willReturn(Boolean.TRUE);

        ResultActions act = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/bookmark/{problemNum}", problemNum)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .header("Authentication", "Bearer {Jwt Access Token}"));
        act.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("bookmark/check",
                        preprocessRequest(),
                        preprocessResponse(),
                        pathParameters(
                                parameterWithName("problemNum").description("문제 번호")
                        ),
                        responseFields(
                                fieldWithPath("isBookmarked").description("북마크 여부")
                        )
                ));
    }

    @Test
    @DisplayName("[API][DELETE] 북마크 취소")
    @WithMockUser(username = "test@test.com", password = "1234", roles = {"USER", "ADMIN"})
    void deleteBookmark() throws Exception {

        Integer problemNum = 1107;

        ResultActions act = mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/bookmark/{problemNum}", problemNum)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .header("Authentication", "Bearer {Jwt Access Token}"));
        act.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("bookmark/delete",
                        preprocessRequest(),
                        preprocessResponse(),
                        pathParameters(
                                parameterWithName("problemNum").description("문제 번호")
                        )
                ));
    }
}
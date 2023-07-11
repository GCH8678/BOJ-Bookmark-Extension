package com.ch.bojbm.domain.bookmark.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TodayProblemsResponseDto {
    private List<TodayProblemDto> problemList;
}

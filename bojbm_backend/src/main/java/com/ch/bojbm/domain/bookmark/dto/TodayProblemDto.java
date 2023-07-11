package com.ch.bojbm.domain.bookmark.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TodayProblemDto {
    private Integer problemNum;
    private String problemTitle;
}

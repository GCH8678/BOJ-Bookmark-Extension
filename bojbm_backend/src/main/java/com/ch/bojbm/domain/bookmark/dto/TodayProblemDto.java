package com.ch.bojbm.domain.bookmark.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TodayProblemDto {
    private Integer problemNum;
    private String problemTitle;
    private String memo;
}

package com.ch.bojbm.domain.bookmark.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkCreateRequestDto {
    private Integer problemId;
    private String problemTitle;
    private String memo;
    private Integer afterDay;
}

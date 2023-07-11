package com.ch.bojbm.domain.bookmark.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BookmarkCreateRequestDto {
    private Integer problemId;
    private String problemTitle;
    private Integer afterDay;
}

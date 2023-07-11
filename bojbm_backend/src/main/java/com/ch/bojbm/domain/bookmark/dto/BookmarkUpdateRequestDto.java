package com.ch.bojbm.domain.bookmark.dto;
import lombok.*;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BookmarkUpdateRequestDto {
    private Integer problemId;
    private Integer afterDay;
}

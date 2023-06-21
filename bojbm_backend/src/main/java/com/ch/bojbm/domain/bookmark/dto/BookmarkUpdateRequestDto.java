package com.ch.bojbm.domain.bookmark.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkUpdateRequestDto {
    private Integer problemId;
    private Integer afterday;
}

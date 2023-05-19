package com.ch.bojbm.domain.bookmark.dto;

import com.ch.bojbm.domain.bookmark.Bookmark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookmarkListResponseDto {
    private List<BookmarkInListResponseDto> bookmarkList;
}

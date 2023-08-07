package com.ch.bojbm.domain.bookmark.dto;

import com.ch.bojbm.domain.bookmark.Bookmark;
import lombok.*;

import java.util.List;


@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BookmarkListResponseDto {
    private List<BookmarkInListResponseDto> bookmarkList;
}

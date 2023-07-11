package com.ch.bojbm.domain.bookmark.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BookmarkInListResponseDto {
    // 링크, 문제번호
    // TODO: 문제제목등 부가적인 정보도 추가 예정
    private LocalDate notificationDate;
    private Integer problemNum;
}

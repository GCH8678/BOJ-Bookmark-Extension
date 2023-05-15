package com.ch.bojbm.domain.bookmark;

import com.ch.bojbm.domain.bookmark.dto.BookmarkCreateRequestDto;
import com.ch.bojbm.domain.bookmark.dto.BookmarkListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/bookmark")
@RestController
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    /**
     * 북마크 목록 조회
     *
     * @param user
     * @return bookmarkListResponseDto
     */
    @GetMapping("/list")
    public ResponseEntity BookmarkListResponseDto(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(bookmarkService.getBookmarkList(user));
    }
    /**
     * 북마크 등록
     *
     * @param bookmarkCreateRequestDto
     * @return message
     */
    @PostMapping("")
    public ResponseEntity addBookmark(@RequestBody BookmarkCreateRequestDto bookmarkCreateRequestDto, @AuthenticationPrincipal User user){
        bookmarkService.addBookmark(user,bookmarkCreateRequestDto.getProblemNum(),7);
        return ResponseEntity.ok().build();
    }


    /**
     * 북마크 취소
     *
     * @param user
     * @param problemNum
     * @return message
     */
    @DeleteMapping("/{problemNum}")
    public ResponseEntity deleteBookmark(@PathVariable Integer problemNum, @AuthenticationPrincipal User user){
        bookmarkService.deleteBookmark(user,problemNum);

        return ResponseEntity.ok().build();
    }

}

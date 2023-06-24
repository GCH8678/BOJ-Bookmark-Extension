package com.ch.bojbm.domain.bookmark;

import com.ch.bojbm.domain.bookmark.dto.BookmarkCreateRequestDto;
import com.ch.bojbm.domain.bookmark.dto.BookmarkUpdateRequestDto;
import com.ch.bojbm.domain.notification.NotificationService;
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
    private final NotificationService notificationService;

//    /**
//     * Email Test
//     */
//    @GetMapping("/test/notification")
//    public void testMailNotification() throws MessagingException {
//        notificationService.sendNotificationWithMail();
//    }

    /**
     * 북마크 목록 조회
     *
     * @return bookmarkListResponseDto
     */
    @GetMapping("/list")
    public ResponseEntity getBookmarkList(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(bookmarkService.getBookmarkList(user));
    }

    /**
     * 오늘 풀어야할 문제 목록 조회
     *
     * @return TodayProblemsResponseDto
     */
    @GetMapping("/list/today")
    public ResponseEntity getTodayProblemList(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(bookmarkService.getTodayProblemList(user));
    }

    /**
     * 북마크 등록
     *
     * @param bookmarkCreateRequestDto
     * @return message
     */
    @PostMapping("")
    public ResponseEntity addBookmark(@RequestBody BookmarkCreateRequestDto bookmarkCreateRequestDto, @AuthenticationPrincipal User user){
        bookmarkService.addBookmark(user,bookmarkCreateRequestDto.getProblemId(), bookmarkCreateRequestDto.getAfterday());
        return ResponseEntity.ok().build();
    }

    /**
     * 북마크 수정
     *
     * @param bookmarkUpdateRequestDto
     * @return message
     */
    @PutMapping("")
    public synchronized ResponseEntity updateBookmark(@RequestBody BookmarkUpdateRequestDto bookmarkUpdateRequestDto, @AuthenticationPrincipal User user){
        bookmarkService.updateBookmark(user,bookmarkUpdateRequestDto.getProblemId(), bookmarkUpdateRequestDto.getAfterday());
        return ResponseEntity.ok().build();
    }


    /**
     * 북마크 여부 확인
     *
     * @param problemNum
     * @return bollean
     */
    @GetMapping("/{problemNum}")
    public ResponseEntity checkBookmark(@PathVariable Integer problemNum, @AuthenticationPrincipal User user){
        boolean isBookmarked = bookmarkService.checkBookmark(user,problemNum);
        return ResponseEntity.ok(isBookmarked);
    }


    /**
     * 북마크 취소
     *
     * @param problemNum
     * @return message
     */
    @DeleteMapping("/{problemNum}")
    public synchronized ResponseEntity deleteBookmark(@PathVariable Integer problemNum, @AuthenticationPrincipal User user){
        bookmarkService.deleteBookmark(user,problemNum);
        return ResponseEntity.ok().build();
    }

}

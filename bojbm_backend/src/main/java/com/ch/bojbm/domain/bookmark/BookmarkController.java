package com.ch.bojbm.domain.bookmark;

import com.ch.bojbm.domain.bookmark.dto.BookmarkCreateRequestDto;
import com.ch.bojbm.domain.bookmark.dto.BookmarkListResponseDto;
import com.ch.bojbm.domain.notification.NotificationService;
import jakarta.mail.MessagingException;
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
     * @param user
     * @return bookmarkListResponseDto
     */
    @GetMapping("/list")
    public ResponseEntity getBookmarkList(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(bookmarkService.getBookmarkList(user));
    }

    /**
     * 오늘 풀어야할 문제 목록 조회
     *
     * @param user
     * @return bookmarkListResponseDto
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
    //TODO: 이후 7일이 아닌 다양한 선택지 받을 수 있게
    @PostMapping("")
    public ResponseEntity addBookmark(@RequestBody BookmarkCreateRequestDto bookmarkCreateRequestDto, @AuthenticationPrincipal User user){
        bookmarkService.addBookmark(user,bookmarkCreateRequestDto.getProblemId(),7);
        return ResponseEntity.ok().build();
    }

    /**
     * 북마크 여부 확인
     *
     * @param user
     * @param problemNum
     * @return message
     */
    @GetMapping("/{problemNum}")
    public ResponseEntity checkBookmark(@PathVariable Integer problemNum, @AuthenticationPrincipal User user){
        boolean isBookmarked = bookmarkService.checkBookmark(user,problemNum);
        return ResponseEntity.ok(isBookmarked);
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

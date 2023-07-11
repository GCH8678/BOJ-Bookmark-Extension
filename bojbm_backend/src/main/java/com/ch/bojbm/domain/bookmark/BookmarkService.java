package com.ch.bojbm.domain.bookmark;


import com.ch.bojbm.domain.bookmark.dto.*;
import com.ch.bojbm.domain.notification.Notification;
import com.ch.bojbm.domain.notification.NotificationService;
import com.ch.bojbm.domain.user.Users;
import com.ch.bojbm.domain.user.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BookmarkService {
    private final BookmarkJpaRepository bookmarkJpaRepository;
    private final UsersRepository usersRepository;

    private final NotificationService notificationService;

    public BookmarkListResponseDto getAllBookmarks(User user){
        Users currentUsers = getUsers(user.getUsername());
        List<BookmarkInListResponseDto> bookmarks = new ArrayList<>();
        Iterator<Bookmark> iterator = bookmarkJpaRepository.findAllByUsers(currentUsers).iterator();
        while(iterator.hasNext()){
            Bookmark temp = iterator.next();
            BookmarkInListResponseDto dto = BookmarkInListResponseDto.builder().problemNum(temp.getProblemNum()).notificationDate(temp.getNotification().getNotificationDate()).build();
            bookmarks.add(dto);
        }
        return BookmarkListResponseDto.builder()
                .bookmarkList(bookmarks)
                .build();
    }

    public TodayProblemsResponseDto getTodayProblemList(User user){
        Notification todayNotification = notificationService.getTodayNotification(user);
        List<TodayProblemDto> problemList = new ArrayList<>();

         if(todayNotification == null) {
             // todayNotification 이 널인 경우 => 빈값
             return TodayProblemsResponseDto.builder().problemList(problemList).build();
         }

        Set<Bookmark> bookmarks = todayNotification.getBookmarks();
        for(Bookmark bookmark : bookmarks){
            TodayProblemDto dto = TodayProblemDto.builder()
                    .problemTitle(bookmark.getProblemTitle())
                    .problemNum(bookmark.getProblemNum())
                    .build();
                    problemList.add(dto);
        }
        return TodayProblemsResponseDto.builder()
                .problemList(problemList)
                .build();
    }


    public void addBookmark(User user, BookmarkCreateRequestDto dto){

        Users currentUsers = getUsers(user.getUsername());
        LocalDate notificationDate = LocalDate.now().plusDays(dto.getAfterDay());

        Bookmark savedBookmark = bookmarkJpaRepository.findBookmarkByProblemNumAndUsers(dto.getProblemId(),currentUsers);

        if(savedBookmark == null){
            Bookmark newBookmark = Bookmark.builder()
                    .problemNum(dto.getProblemId())
                    .users(currentUsers)
                    .problemTitle(dto.getProblemTitle())
                    .build();
            newBookmark.setNotification(notificationService.addBookmarkInNotification(currentUsers, newBookmark, notificationDate));
            bookmarkJpaRepository.save(newBookmark);
        }else throw new RuntimeException("이미 존재하는 북마크입니다.");
    }

    public boolean checkBookmark(User user, Integer problemNum){
        Users currentUsers = getUsers(user.getUsername());
        Bookmark savedBookmark = bookmarkJpaRepository.findBookmarkByProblemNumAndUsers(problemNum,currentUsers);

        if(savedBookmark == null){
            return false;
        }else{
            return true;
        }

    }

    @Transactional
    public void deleteBookmark(User user, Integer problemNum) {
        
        Users currentUsers = getUsers(user.getUsername());
        Bookmark savedBookmark = bookmarkJpaRepository.findBookmarkByProblemNumAndUsers(problemNum,currentUsers);
        Notification notification = savedBookmark.getNotification();

        if(savedBookmark != null){
            bookmarkJpaRepository.delete(savedBookmark);
            bookmarkJpaRepository.flush();
            notificationService.checkAndDeleteNotification(notification);
        }else throw new RuntimeException("이미 존재하지 않는 북마크입니다.");


    }


    @Transactional
    public void updateBookmark(User user, BookmarkUpdateRequestDto dto){
        Users currentUsers = getUsers(user.getUsername());
        LocalDate newNotificationDate = LocalDate.now().plusDays(dto.getAfterDay());

        Bookmark savedBookmark = bookmarkJpaRepository.findBookmarkByProblemNumAndUsers(dto.getProblemId(),currentUsers);
        Notification oldNotification = savedBookmark.getNotification();
        if (savedBookmark != null){
            savedBookmark.setNotification(notificationService.addBookmarkInNotification(currentUsers,savedBookmark,newNotificationDate));
            bookmarkJpaRepository.saveAndFlush(savedBookmark);
            notificationService.checkAndDeleteNotification(oldNotification);
        }else throw new RuntimeException("잘못된 요청입니다.(저장된 북마크가 아니라 수정이 불가함)");

    }


    private Users getUsers(String email) {
        Optional<Users> member = usersRepository.findByEmail(email);
        if (member.isPresent()) {
            return member.get();
        } else throw new EntityNotFoundException("유저를 찾을 수 없습니다.");
    }

}
